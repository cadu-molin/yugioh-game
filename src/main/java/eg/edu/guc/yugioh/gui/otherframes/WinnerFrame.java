package eg.edu.guc.yugioh.gui.otherframes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.gui.GUI;
import eg.edu.guc.yugioh.gui.Main;

@SuppressWarnings("serial")
public class WinnerFrame extends JFrame implements ActionListener{

	private static final String TITLE = "Winner!";
	private static final String REPLAY_LABEL = "Replay";
	private static final String EXIT_LABEL = "Exit Game";
	private static final String CONGRATS_PREFIX = " Congratulations ";
	private static final String CONGRATS_SUFFIX = ", You won!";
	private static final int FRAME_WIDTH = 350;
	private static final int FRAME_HEIGHT = 120;
	private static final int WINNER_FONT_SIZE = 18;

	JLabel winner = new JLabel();
	JButton replayButton = new JButton(REPLAY_LABEL);
	JButton exitGameButton = new JButton(EXIT_LABEL);
	
	public WinnerFrame(){
		super(TITLE);
		GUI.getBoardFrame().dispose();
		winner.setText(CONGRATS_PREFIX + Card.getBoard().getWinner().getName() + CONGRATS_SUFFIX);
		winner.setFont(new Font(winner.getFont().getName(), Font.PLAIN, WINNER_FONT_SIZE));
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new GridBagLayout());
		setVisible(true);
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		replayButton.addActionListener(this);
		exitGameButton.addActionListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridwidth = 2;
		add(winner , c);
		c.insets.top = 15;
		c.insets.left = 30;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 1;
		add(replayButton , c);
		c.gridx = 1;
		add(exitGameButton , c);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals(REPLAY_LABEL)){
			GUI.getWinnerFrame().dispose();
			Main.startNewGame();
		}else if(arg0.getActionCommand().equals(EXIT_LABEL)) {
			System.exit(0);
		}
	}
}
