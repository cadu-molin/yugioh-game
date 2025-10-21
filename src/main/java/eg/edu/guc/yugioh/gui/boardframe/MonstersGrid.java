package eg.edu.guc.yugioh.gui.boardframe;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.Mode;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;
import eg.edu.guc.yugioh.configsGlobais.Logger;

@SuppressWarnings("serial")
public class MonstersGrid extends JPanel {
	private MonsterButton [] monsterButtons;
	private ArrayList<MonsterCard> monstersArea= new ArrayList<MonsterCard>();
	private boolean active;
	public MonstersGrid(boolean active) {
		setLayout(new GridLayout(1, GameConstants.MAX_MONSTERS_ON_FIELD));
		monsterButtons = new MonsterButton [GameConstants.MAX_MONSTERS_ON_FIELD];
		this.active=active;
			for(int i = 0; i<GameConstants.MAX_MONSTERS_ON_FIELD ; i++){
			monsterButtons[i]= new MonsterButton();
			add(monsterButtons[i]);     
		}

		setPreferredSize(new Dimension(475,150));
		validate();
	}
	
	public void updateMonstersArea() {

		Logger.logs().info("MonstersGrid - updateMonstersArea active: " + active);

		removeAll();
		if(active){
			monstersArea = Card.getBoard().getActivePlayer().getField().getMonstersArea();
		}else{
			monstersArea = Card.getBoard().getOpponentPlayer().getField().getMonstersArea();
		}
		for (int i = 0; i < GameConstants.MAX_MONSTERS_ON_FIELD; i++) {
			if(i<monstersArea.size()){
				MonsterCard addedMonster = monstersArea.get(i);
				monsterButtons[i] = new MonsterButton(addedMonster);
				if(addedMonster.getMode()==Mode.DEFENSE)
					monsterButtons[i].toDefence();
				if(addedMonster.isHidden() && !active)
					monsterButtons[i].setToolTipText(null);
				add(monsterButtons[i]);
			}
			else add(new MonsterButton());
		}
		repaint();
		validate();
	}
	
	public MonsterButton[] getMonsterButtons() {
		return monsterButtons;
	}
	public void setMonsterButtons(MonsterButton[] monsterButtons) {
		this.monsterButtons = monsterButtons;
	}
}