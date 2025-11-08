package eg.edu.guc.yugioh.gui.boardframe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import eg.edu.guc.yugioh.board.player.Phase;
import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;

@SuppressWarnings("serial")
public class PlayerNamePanel extends JPanel {
	private JLabel playerNameLabel = new JLabel();
	private JLabel lifePointsLabel = new JLabel();
	private JLabel currentPhaseLabel = new JLabel();
	private boolean active;

	public PlayerNamePanel(boolean active) {
		this.active = active;

		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(200, 120));

		addPanels();
		validate();
	}

	private void addPanels() {

		adjustPlayerNameLabel();
		adjustLifePointsLabel();
		adjustCurrentPhaseLabel();

		addLabels();

		updateAll();
	}

	public void adjustPlayerNameLabel() {
		playerNameLabel.setFont(new Font("Cambria", Font.ITALIC | Font.BOLD, 20));
		playerNameLabel.setForeground(Color.BLACK);
		playerNameLabel.setOpaque(true);
		playerNameLabel.setBackground(new Color(255, 255, 255, 255));
		playerNameLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		playerNameLabel.setAlignmentX(LEFT_ALIGNMENT);
		playerNameLabel.setMaximumSize(new Dimension(190, 35));
		LineBorder line = new LineBorder(new Color(255, 255, 255, 128), 2);
		EmptyBorder margin = new EmptyBorder(5, 10, 5, 10);
		playerNameLabel.setBorder(new CompoundBorder(line, margin));
	}

	public void adjustCurrentPhaseLabel() {
		currentPhaseLabel.setFont(new Font("Cambria", Font.ITALIC | Font.BOLD, 20));
		currentPhaseLabel.setForeground(Color.WHITE);
		currentPhaseLabel.setOpaque(true);
		currentPhaseLabel.setBackground(new Color(0, 0, 0, 128));
		currentPhaseLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		currentPhaseLabel.setAlignmentX(LEFT_ALIGNMENT);
		currentPhaseLabel.setMaximumSize(new Dimension(120, 35));
	}

	public void adjustLifePointsLabel() {

		lifePointsLabel.setFont(new Font("Cambria", Font.BOLD, 20));
		lifePointsLabel.setForeground(Color.WHITE);
		lifePointsLabel.setOpaque(true);
		lifePointsLabel.setAlignmentX(LEFT_ALIGNMENT);
		lifePointsLabel.setMaximumSize(new Dimension(150, 35));
		lifePointsLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
	}

	public void addLabelsCrescentOrder() {
		add(currentPhaseLabel);
		add(Box.createRigidArea(new Dimension(0, 0)));
		add(lifePointsLabel);
		add(Box.createRigidArea(new Dimension(0, 0)));
		add(playerNameLabel);
	}

	public void addLabelsDescentOrder() {
		add(playerNameLabel);
		add(Box.createRigidArea(new Dimension(0, 0)));
		add(lifePointsLabel);
		add(Box.createRigidArea(new Dimension(0, 0)));
		add(currentPhaseLabel);
	}

	public void addLabels() {

		if( active ) {
			addLabelsCrescentOrder();
			return;
		}

		addLabelsDescentOrder();
	}

	public JLabel getplayerNameLabel() {
		return playerNameLabel;
	}

	public JLabel getlifePointsLabel() {
		return lifePointsLabel;
	}

	public JLabel getCurrentPhaseLabel() {
		return currentPhaseLabel;
	}

	public void updateAll() {
		updatePhase();
		updatePlayerName();
		updateLifePoints();
		updateColorHud();
	}

	public void updatePhase() {
		Phase current = Card.getBoard().getActivePlayer().getField().getPhase();
		if (current.equals(Phase.MAIN1))
			currentPhaseLabel.setText("Main 1");
		if (current.equals(Phase.MAIN2))
			currentPhaseLabel.setText("Main 2");
		if (current.equals(Phase.BATTLE))
			currentPhaseLabel.setText("Battle");
		if (!active) {
			currentPhaseLabel.setText("Inactive");
		}
	}

	public void updateColorHud() {

		if (active)
			lifePointsLabel.setBackground(Card.getBoard().getActivePlayer().getColorHud());
		else
			lifePointsLabel.setBackground(Card.getBoard().getOpponentPlayer().getColorHud());

	}

	public void updatePlayerName() {
		if (active)
			playerNameLabel.setText(Card.getBoard().getActivePlayer().getName());
		else
			playerNameLabel.setText(Card.getBoard().getOpponentPlayer().getName());
	}

	public void updateLifePoints() {
		if (active)
			lifePointsLabel.setText("HP " + Card.getBoard().getActivePlayer().getLifePoints());
		else
			lifePointsLabel.setText("HP " + Card.getBoard().getOpponentPlayer().getLifePoints());
	}

	// 🔹 Teste isolado
	public static void main(String[] args) {
		JFrame frame = new JFrame("Teste PlayerNamePanel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);

		PlayerNamePanel panel = new PlayerNamePanel(true);
		panel.getplayerNameLabel().setText("Jogador 1");
		panel.getlifePointsLabel().setText("HP " + GameConstants.INITIAL_LIFE_POINTS);
		panel.getCurrentPhaseLabel().setText("Main 1");

		frame.add(panel);
		frame.setVisible(true);
	}
}
