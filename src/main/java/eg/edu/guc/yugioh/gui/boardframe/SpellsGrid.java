package eg.edu.guc.yugioh.gui.boardframe;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;

@SuppressWarnings("serial")
public class SpellsGrid extends JPanel {
	private SpellButton [] spellsButtons;
	private ArrayList<SpellCard> spellsArea;
	private boolean active;
	public SpellsGrid(boolean active) {
		setLayout(new GridLayout(1, GameConstants.MAX_SPELLS_ON_FIELD));
		spellsButtons = new SpellButton [GameConstants.MAX_SPELLS_ON_FIELD];
		this.active = active;
		for(int i = 0; i<GameConstants.MAX_SPELLS_ON_FIELD ; i++){
			spellsButtons[i]= new SpellButton();
			add(spellsButtons[i]);
		}
		setPreferredSize(new Dimension(475,150));
		validate();
	}
	public void updateSpellsArea() {

		removeAll();
		if(active){
			spellsArea = Card.getBoard().getActivePlayer().getField().getSpellArea();
		
		for (int i = 0; i < GameConstants.MAX_SPELLS_ON_FIELD; i++) {
			if(i<spellsArea.size()){
				spellsButtons[i] = new SpellButton(spellsArea.get(i));
				add(spellsButtons[i]);
			}else add(new SpellButton());
		}
		}else{
			spellsArea = Card.getBoard().getOpponentPlayer().getField().getSpellArea();
			for (int i = 0; i < GameConstants.MAX_SPELLS_ON_FIELD; i++) {
				if(i<spellsArea.size()){
					SpellButton addedSpell = new SpellButton();
					addedSpell.setIcon(new ImageIcon("images/AttackMode.png"));
					spellsButtons[i] = addedSpell;
					add(spellsButtons[i]);
				}else add(new SpellButton());
			}
		}
		repaint();
		validate();
	}
	public SpellButton[] getSpellsButtons() {
		return spellsButtons;
	}
	public void setSpellsButtons(SpellButton[] spellsButtons) {
		this.spellsButtons = spellsButtons;
	}
}