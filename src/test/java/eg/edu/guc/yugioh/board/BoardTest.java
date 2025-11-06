package eg.edu.guc.yugioh.board;

import eg.edu.guc.yugioh.board.player.Player;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    private Player p1;
    private Player p2;
    private Board board;

    @BeforeEach
    void setup() throws IOException, UnexpectedFormatException {
        p1 = new Player("P1", Color.BLUE, "");
        p2 = new Player("P2", Color.RED, "");
        board = new Board();
    }

    @Test
    void startGame_shouldDealHandsAndActiveDrawsExtra() {
        board.startGame(p1, p2);

        int h1 = p1.getField().getHand().size();
        int h2 = p2.getField().getHand().size();

        assertThat(h1 + h2).isEqualTo(11); // 5 + 5 + 1 extra to active
        assertThat(Math.max(h1, h2)).isEqualTo(6);
        assertThat(Math.min(h1, h2)).isEqualTo(5);

        assertThat(board.getActivePlayer()).isNotNull();
        assertThat(board.getOpponentPlayer()).isNotNull();
    }

    @Test
    void nextPlayer_shouldRotateAndDrawCard() {
        board.startGame(p1, p2);
        board.nextPlayer();

        assertThat(p1.getField().getHand().size()).isEqualTo(6);
        assertThat(p2.getField().getHand().size()).isEqualTo(6);
        assertThat(board.getActivePlayer()).isNotNull();
        assertThat(board.getOpponentPlayer()).isNotNull();
    }

    @Test
    void setWinner_shouldNotifyListenersOnce() {
        AtomicReference<Player> notified = new AtomicReference<>();
        board.addWinnerListener(notified::set);

        board.setWinner(p1);
        assertThat(board.getWinner()).isEqualTo(p1);
        assertThat(notified.get()).isEqualTo(p1);


        board.setWinner(p2);
        assertThat(board.getWinner()).isEqualTo(p1);
        assertThat(notified.get()).isEqualTo(p1);
    }
}
