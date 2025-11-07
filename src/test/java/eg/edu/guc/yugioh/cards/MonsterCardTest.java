package eg.edu.guc.yugioh.cards;

import eg.edu.guc.yugioh.board.Board;
import eg.edu.guc.yugioh.board.player.Player;
import eg.edu.guc.yugioh.board.player.Phase;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MonsterCardTest {

    private Player p1;
    private Player p2;

    @BeforeEach
    void setup() throws IOException, UnexpectedFormatException {
        p1 = new Player("P1", Color.BLUE, "");
        p2 = new Player("P2", Color.RED, "");
        Board board = new Board();
        board.setActivePlayer(p1);
        board.setOpponentPlayer(p2);
    }

    private MonsterCard putMonsterOnField(Player player, String name, int level, int atk, int def, Mode mode) {
        MonsterCard m = new MonsterCard(name, "", level, atk, def);
        player.getField().getHand().add(m);
        m.setLocation(Location.HAND);
        if (mode == Mode.ATTACK) {
            player.summonMonster(m);
        } else {
            player.setMonster(m);
        }
        return m;
    }

    @Test
    void switchMode_shouldToggleAndSetHiddenAccordingly() {
        MonsterCard m = new MonsterCard("Alpha", "", 4, 1000, 1000);
        assertThat(m.getMode()).isEqualTo(Mode.DEFENSE);
        m.switchMode();
        assertThat(m.getMode()).isEqualTo(Mode.ATTACK);
        assertThat(m.isHidden()).isFalse();
        m.switchMode();
        assertThat(m.getMode()).isEqualTo(Mode.DEFENSE);
        assertThat(m.isHidden()).isTrue();
    }
}
