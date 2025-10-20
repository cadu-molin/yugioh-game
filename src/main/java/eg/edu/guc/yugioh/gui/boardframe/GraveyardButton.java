package eg.edu.guc.yugioh.gui.boardframe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import eg.edu.guc.yugioh.board.player.Player;
import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.configsGlobais.Logger;

@SuppressWarnings("serial")
public class GraveyardButton extends JButton implements ActionListener {
	private static ImageIcon graveyard = new ImageIcon("images/Graveyard.png");
	private boolean active ;
	private final Map<Boolean, JPanel> paineisPrincipais = new HashMap<>();

	private JPanel getPainel(boolean active) {
		return paineisPrincipais.computeIfAbsent(active, k -> new JPanel());
	}


	public GraveyardButton(boolean active) {
		super(graveyard);
		this.active=active;
		addActionListener(this);
		setPreferredSize(new Dimension(CardButton.getDimension('W'),150));
	}


	public void updateGraveyard(){
		ArrayList<Card> graveyardList ;

		Logger.logs().info("GraveyardButton - updateGraveyard active: " + active);

		if(active)
			graveyardList = Card.getBoard().getActivePlayer().getField().getGraveyard();
		else graveyardList = Card.getBoard().getOpponentPlayer().getField().getGraveyard();
		if(graveyardList.size()>0){
			Card current = graveyardList.get(graveyardList.size()-1);

			boolean isInstanceMonster = current instanceof MonsterCard;

			Logger.logs().info("GraveyardButton - updateGraveyard isInstanceMonster: " + isInstanceMonster);

			if(isInstanceMonster){
				setIcon(new ImageIcon("images/"+current.getName()+".jpg"));
				setToolTipText(current.getName()+"\n ATK: "+((MonsterCard)current).getAttackPoints()+"\n DEF: "+((MonsterCard)current).getDefensePoints()+"\n Level: "+((MonsterCard)current).getLevel());
			}
			else{ setIcon(new ImageIcon("images/"+current.getName()+".png"));
				setToolTipText(current.getName());
			}
		}
		else setIcon(graveyard);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Player targetPlayer = active
				? Card.getBoard().getActivePlayer()
				: Card.getBoard().getOpponentPlayer();

		exibirGraveyard(targetPlayer);
	}

	private void exibirGraveyard(Player player) {
		ArrayList<Card> graveyard = player.getField().getGraveyard();
		if (graveyard.isEmpty()) return;

		int count = graveyard.size();
		JPanel painel = criarPanel(Math.ceil(count / 5.0));
		JFrame janela = new JFrame();

		for (Card card : graveyard) {
			painel.add(criarBotaoCarta(card));
		}

		abrirPanel(painel, janela);
	}

	private JComponent criarBotaoCarta(Card card) {
		return (card instanceof MonsterCard)
				? new MonsterButton((MonsterCard) card)
				: new SpellButton((SpellCard) card);
	}

	private JPanel criarPanel(Double quantidade){
		GridLayout gridLayout = new GridLayout((int) Math.ceil(quantidade), 5);
		return new JPanel(gridLayout);
	}
	 private void abrirPanel(JPanel painelPrincipal,JFrame janela){

		 painelPrincipal.setPreferredSize(new Dimension(475,165));
		 janela.getContentPane().add(painelPrincipal);
		 janela.pack();
		 janela.setLocationRelativeTo(null);
		 janela.setVisible(true);
	 }

}
