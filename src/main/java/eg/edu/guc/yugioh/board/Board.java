package eg.edu.guc.yugioh.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eg.edu.guc.yugioh.board.player.Player;
import eg.edu.guc.yugioh.cards.Card;

public class Board {

	private Player activePlayer;
	private Player opponentPlayer;
	private Player winner;
	private final List<WinnerListener> winnerListeners = new ArrayList<WinnerListener>();

	public Board() {

		Card.setBoard(this);

	}

	public void whoStarts(Player p1, Player p2) {

		Random r = new Random();

		if (r.nextInt(2) == 0) {
			activePlayer = p1;
			opponentPlayer = p2;
		} else {
			activePlayer = p2;
			opponentPlayer = p1;
		}

	}

	public void startGame(Player p1, Player p2) {

		p1.addNCardsToHand(5);
		p2.addNCardsToHand(5);

		whoStarts(p1, p2);

		activePlayer.addCardToHand();

	}

	public void nextPlayer() {

		Player temp = activePlayer;
		activePlayer = opponentPlayer;
		opponentPlayer = temp;
		activePlayer.addCardToHand();

	}

	public boolean isGameOver() {
		if (winner != null)
			return true;
		return false;
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(Player activePlayer) {
		this.activePlayer = activePlayer;
	}

	public Player getOpponentPlayer() {
		return opponentPlayer;
	}

	public void setOpponentPlayer(Player opponentPlayer) {
		this.opponentPlayer = opponentPlayer;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		if (isGameOver())
			return;
		this.winner = winner;
		notifyWinnerDeclared(winner);
	}

	public void addWinnerListener(WinnerListener listener) {
		if (listener != null && !winnerListeners.contains(listener)) {
			winnerListeners.add(listener);
		}
	}

	public void removeWinnerListener(WinnerListener listener) {
		winnerListeners.remove(listener);
	}

	private void notifyWinnerDeclared(Player winner) {
		for (WinnerListener winnerListener : winnerListeners) {
            winnerListener.onWinnerDeclared(winner);
		}
	}

}
