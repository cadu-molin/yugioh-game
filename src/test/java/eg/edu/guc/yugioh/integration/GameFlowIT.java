package eg.edu.guc.yugioh.integration;

import eg.edu.guc.yugioh.board.Board;
import eg.edu.guc.yugioh.board.player.Player;
import eg.edu.guc.yugioh.board.player.Phase;
import eg.edu.guc.yugioh.cards.Location;
import eg.edu.guc.yugioh.cards.Mode;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GameFlowIT {

    private Player p1;
    private Player p2;
    private Board board;

    @BeforeEach
    void setup() throws IOException, UnexpectedFormatException {
        p1 = new Player("P1", Color.BLUE, "");
        p2 = new Player("P2", Color.RED, "");
        board = new Board();
        board.startGame(p1, p2);
    }

    @Test
    void simpleTurn_attackDirectly_reducesOpponentLifePoints() {
        board.setActivePlayer(p1);
        board.setOpponentPlayer(p2);

        MonsterCard attacker = new MonsterCard("Attacker", "", 4, 1500, 1000);
        p1.getField().getHand().add(attacker);
        attacker.setLocation(Location.HAND);
        boolean summoned = p1.summonMonster(attacker);
        assertThat(summoned).isTrue();

        p1.getField().setPhase(Phase.BATTLE);
        boolean attacked = p1.declareAttack(attacker);
        assertThat(attacked).isTrue();

        assertThat(p2.getLifePoints()).isEqualTo(8000 - 1500);
    }
}
