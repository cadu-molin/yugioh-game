package eg.edu.guc.yugioh.gui.boardframe;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class PlayerAreaPanel extends JPanel {
	private MonstersSpellsPanel monsterSpellPanel;
	private DeckGraveyard deckGraveyardPanel;
	private PlayerNamePanel playerNamePanel;
	private final JLabel activeImage;
	private final boolean active;

	public PlayerAreaPanel(boolean ao){
		super();
		setOpaque(false);
		active = ao;
		monsterSpellPanel = new MonstersSpellsPanel(ao);
		deckGraveyardPanel = new DeckGraveyard(ao);

		JPanel containerLabel = new JPanel();
		containerLabel.setOpaque(false);
		containerLabel.setLayout(new BoxLayout(containerLabel, BoxLayout.X_AXIS));

		playerNamePanel = new PlayerNamePanel(ao);
		activeImage = new JLabel();
		updatePlayerImage();

		containerLabel.add(activeImage);
		containerLabel.add(playerNamePanel);

		add(containerLabel, BorderLayout.WEST);
		add(monsterSpellPanel,BorderLayout.CENTER);
		add(deckGraveyardPanel,BorderLayout.EAST);
		setPreferredSize(new Dimension(900,320));
		validate();

	}

	public void updatePlayerImage() {

		if (active) {
			activeImage.setIcon(resizeIcon(Card.getBoard().getActivePlayer().getImagePath(), GameConstants.PLAYER_IMAGE_SIZE, GameConstants.PLAYER_IMAGE_SIZE));
			activeImage.setBorder(new LineBorder(Card.getBoard().getActivePlayer().getColorHud(), GameConstants.BORDER_WIDTH));
			return;
		}

		activeImage.setIcon(resizeIcon(Card.getBoard().getOpponentPlayer().getImagePath(), GameConstants.PLAYER_IMAGE_SIZE, GameConstants.PLAYER_IMAGE_SIZE));
		activeImage.setBorder(new LineBorder(Card.getBoard().getOpponentPlayer().getColorHud(), GameConstants.BORDER_WIDTH));

	}

	public void updateAll() {

		playerNamePanel.updateAll();
		updatePlayerImage();

	}

	private ImageIcon resizeIcon(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	public MonstersSpellsPanel getMonsterSpellPanel() {
		return monsterSpellPanel;
	}
	public void setMonsterSpellPanel(MonstersSpellsPanel monsterSpellPanel) {
		this.monsterSpellPanel = monsterSpellPanel;
	}
	public DeckGraveyard getDeckGraveyardPanel() {
		return deckGraveyardPanel;
	}
	public void setDeckGraveyardPanel(DeckGraveyard deckGraveyardPanel) {
		this.deckGraveyardPanel = deckGraveyardPanel;
	}
	public PlayerNamePanel getPlayerNamePanel() {
		return playerNamePanel;
	}

	public void setPlayerNamePanel(PlayerNamePanel playerNamePanel) {
		this.playerNamePanel = playerNamePanel;
	}

    public void updateAllPanels() {
        this.getDeckGraveyardPanel().getDeck().updateDeck();
        this.getDeckGraveyardPanel().getGraveyard().updateGraveyard();
        this.getMonsterSpellPanel().getMonstersGrid().updateMonstersArea();
        this.getMonsterSpellPanel().getSpellsGrid().updateSpellsArea();
        this.getPlayerNamePanel().updateLifePoints();
    }

}