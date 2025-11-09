package eg.edu.guc.yugioh.gui.boardframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import eg.edu.guc.yugioh.board.player.Phase;
import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.Location;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.exceptions.IllegalSpellTargetException;
import eg.edu.guc.yugioh.exceptions.GameException;
import eg.edu.guc.yugioh.gui.GUI;

@SuppressWarnings("serial")
public class MonsterButton extends CardButton implements ActionListener{

	private MonsterCard monster;
	public MonsterButton() {
		super(Attack);
		addActionListener(null);
	}

	public MonsterButton(MonsterCard monster) {
		super(Attack);
		ImageIcon icon = new ImageIcon("images/"+monster.getName()+".jpg");
		if(icon!=null){
			setIcon(icon);
		}
		this.monster = monster;
		String monsterInfo = String.format("<html>%s<br>ATK: %d<br>DEF: %d<br>Level: %d</html>", monster.getName(), monster.getAttackPoints(), monster.getDefensePoints(), monster.getLevel());
		setToolTipText(monsterInfo);
		// Mantém tooltip no hover original
		// Atualiza painel lateral ao passar o mouse e ao clicar
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (GUI.getBoardFrame() != null && GUI.getBoardFrame().getCardInfoPanel() != null) {
					GUI.getBoardFrame().getCardInfoPanel().displayCardInfo(monster);
				}
			}
		});
		addActionListener(this);
		validate();
	}

	public void toDefence(){
		setIcon(Defence);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		Logger.logs().info("MonsterButton - actionPerformed ");
		// Atualiza o painel lateral ao clicar
		if (GUI.getBoardFrame() != null && GUI.getBoardFrame().getCardInfoPanel() != null) {
			GUI.getBoardFrame().getCardInfoPanel().displayCardInfo(monster);
		}

		if(this.monster.isCardInField()){
			if(Card.getBoard().getActivePlayer().getField().getPhase()==Phase.BATTLE){
				battlePhaseActions();
				return;
			}else{
				if(GUI.getBoardFrame().isToSwitch()){
					switchMonsterModeAction();
				}
			}
			if(GUI.getBoardFrame().isSummoningSacrificeMonster()){
				sacrificesAction();
			}
			if(GUI.getBoardFrame().getSpellToActivate() != null){
				spellActivationWithTargetAction();
			}
			GUI.getBoardFrame().updateBoardFrame();
		}

		if(this.monster.isCardInHand()){
			new HandOptionsFrame(true,monster);
		}
	}

	private void switchMonsterModeAction() {
		Card.getBoard().getActivePlayer().switchMonsterMode(monster);
		GUI.getBoardFrame().setToSwitch(false);
	}

	private void spellActivationWithTargetAction() {
		try{
			Card.getBoard().getActivePlayer().activateSpell(GUI.getBoardFrame().getSpellToActivate(), monster);
			GUI.getBoardFrame().setSpellToActivate(null);
		}catch(IllegalSpellTargetException e){
			GUI.errorFrame(e);
		}
	}

	private void sacrificesAction() {
        GUI.getBoardFrame().sacrificeMonster(monster);
	}

	private void battlePhaseActions() {
		//bug start fix
		if(GUI.getBoardFrame().getSpellToActivate()!=null){
			GUI.getBoardFrame().setSpellToActivate(null);
			GUI.errorFrame(new GameException(GameException.Type.WRONG_PHASE));
		}
		//bug end fix
		if(GUI.getBoardFrame().getAttackingMonster()!=null){
			Card.getBoard().getActivePlayer().declareAttack(GUI.getBoardFrame().getAttackingMonster(), monster);
			GUI.getBoardFrame().setAttackingMonster(null);
		}
		else{
			try{
				if(!Card.getBoard().getActivePlayer().declareAttack(monster) ){ // attacks life if possible
					if(Card.getBoard().getActivePlayer().getField().getMonstersArea().contains(monster)){
					new ConfirmFrame("Please choose a monster to attack");
					GUI.getBoardFrame().setAttackingMonster(monster);
					}
				}
			}catch(Exception e){
				GUI.getBoardFrame().resetHandlers();
				GUI.errorFrame(e);
			}
		}
		GUI.getBoardFrame().updateBoardFrame();
	}
}