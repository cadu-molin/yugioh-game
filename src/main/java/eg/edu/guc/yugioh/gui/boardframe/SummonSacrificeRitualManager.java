package eg.edu.guc.yugioh.gui.boardframe;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;
import eg.edu.guc.yugioh.gui.GUI;

import java.util.ArrayList;

public class SummonSacrificeRitualManager {
    private MonsterCard monsterToSummon;
    private int sacrificesCount;
    private ArrayList<MonsterCard> sacrificedMonsters = new ArrayList<MonsterCard>();

    public boolean isSacrificeAttack() {
        return sacrificeAttack;
    }

    public void setSacrificeAttack(boolean sacrificeAttack) {
        this.sacrificeAttack = sacrificeAttack;
    }

    private boolean sacrificeAttack;


    public int getSacrificesCount() {
        return sacrificesCount;
    }

    public void setSacrificesCount(int sacrificesCount) {
        this.sacrificesCount = sacrificesCount;
    }

    public void decrementSacrificesCount(){
        this.sacrificesCount--;
    }

    public void startRitual(MonsterCard monsterToSummon, boolean isToSummonInAttackMode) throws Exception {
        int sacrificesNeeded = monsterToSummon.getLevel() < GameConstants.MIN_LEVEL_TWO_SACRIFICES ? GameConstants.ONE_SACRIFICE : GameConstants.TWO_SACRIFICES;
        this.setSacrificesCount(sacrificesNeeded);

        if (Card.getBoard().getActivePlayer().getField().getMonstersArea().size() >= sacrificesNeeded) {
            new ConfirmFrame("Please click " + sacrificesNeeded + " monster(s) to sacrifice");
            this.setMonsterToSummon(monsterToSummon);
            this.setSacrificeAttack(isToSummonInAttackMode);
        } else {
            throw new Exception("You don't have enough sacrifices.");
        }
    }

    public void sacrificeMonster(MonsterCard monsterToSacrifice){
        if(this.getSacrificesCount() >0){
            if(!this.getSacrificedMonsters().contains(monsterToSacrifice)){
                this.getSacrificedMonsters().add(monsterToSacrifice);
                this.decrementSacrificesCount();
            } // bug needed brackets
        }

        if(this.getSacrificesCount() == 0){
            try{
                if(this.isSacrificeAttack())
                    Card.getBoard().getActivePlayer().summonMonster(this.getMonsterToSummon(),this.getSacrificedMonsters());
                else
                    Card.getBoard().getActivePlayer().setMonster(this.getMonsterToSummon(),this.getSacrificedMonsters());
                this.setMonsterToSummon(null);
            }catch(Exception e){
                GUI.errorFrame(e);
            }
            finally{ // added finally
                this.setSacrificedMonsters(new ArrayList<MonsterCard>());
            }
        }
    }

    public void reset(){
        this.sacrificedMonsters = new ArrayList<MonsterCard>();
        this.sacrificesCount = 0;
        this.monsterToSummon = null;
        this.sacrificeAttack = false;
    }

    public MonsterCard getMonsterToSummon() {
        return monsterToSummon;
    }

    public void setMonsterToSummon(MonsterCard monsterToSummon) {
        this.monsterToSummon = monsterToSummon;
    }

    public ArrayList<MonsterCard> getSacrificedMonsters() {
        return sacrificedMonsters;
    }

    public void setSacrificedMonsters(ArrayList<MonsterCard> sacrificedMonsters) {
        this.sacrificedMonsters = sacrificedMonsters;
    }

    public boolean isSummoningSacrificeMonster(){
        return this.getMonsterToSummon() != null;
    }
}
