/**
 * @author Tokyo
 */
package eg.edu.guc.yugioh.gui;

import eg.edu.guc.yugioh.board.Board;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.gui.listener.WinnerGUIListener;

public class Main {
	@SuppressWarnings("unused")
	public static void startNewGame() {

		Logger.logs().info("Main - startNewGame");

		Board board = new Board();
		GUI gui = new GUI();

        board.addWinnerListener(new WinnerGUIListener());
	}

	public static void main(String[] args) {
		startNewGame();
	}
}