/**
 * @author Tokyo
 */
package eg.edu.guc.yugioh.gui;

import eg.edu.guc.yugioh.board.Board;
import eg.edu.guc.yugioh.board.WinnerListener;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.gui.otherframes.WinnerFrame;

public class Main {
	@SuppressWarnings("unused")
	public static void startNewGame() {

		Logger.logs().info("Main - startNewGame");

		Board board = new Board();
		GUI gui = new GUI();

        board.addWinnerListener(new WinnerListener() {
			@Override
			public void onWinnerDeclared(eg.edu.guc.yugioh.board.player.Player winner) {
				WinnerFrame x = new WinnerFrame();
				GUI.setWinnerFrame(x);
			}
		});
	}

	public static void main(String[] args) {
		startNewGame();
	}
}