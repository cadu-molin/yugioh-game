package eg.edu.guc.yugioh.board;

import eg.edu.guc.yugioh.board.player.Player;

/**
 * Domain-level listener to announce that a winner has been declared.
 * This decouples the game engine (domain) from the UI.
 */
public interface WinnerListener {
    void onWinnerDeclared(Player winner);
}
