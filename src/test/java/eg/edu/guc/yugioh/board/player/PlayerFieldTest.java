package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.board.Board;
import eg.edu.guc.yugioh.cards.Location;
import eg.edu.guc.yugioh.cards.Mode;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.exceptions.MultipleMonsterAdditionException;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;
import eg.edu.guc.yugioh.exceptions.WrongPhaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class PlayerFieldTest {

    private Player p1;
    private Player p2;
    private Board board;

    @BeforeEach
    void setup() throws IOException, UnexpectedFormatException {
        p1 = new Player("P1", Color.BLUE, "");
        p2 = new Player("P2", Color.RED, "");
        board = new Board();
        // Deterministic: make p1 active manually
        board.setActivePlayer(p1);
        board.setOpponentPlayer(p2);
    }

    private MonsterCard putMonsterInHand(Player player, String name, int level, int atk, int def) {
        MonsterCard m = new MonsterCard(name, "", level, atk, def);
        player.getField().getHand().add(m);
        m.setLocation(Location.HAND);
        return m;
    }

    @Test
    void summonMonster_oncePerTurn_enforced() {
        MonsterCard m1 = putMonsterInHand(p1, "Alpha", 4, 1500, 1200);
        boolean first = p1.summonMonster(m1);
        assertThat(first).isTrue();
        MonsterCard m2 = putMonsterInHand(p1, "Beta", 4, 1400, 1000);
        assertThatThrownBy(() -> p1.summonMonster(m2))
                .isInstanceOf(MultipleMonsterAdditionException.class);
    }

    @Test
    void summonMonster_requiresActivePlayer_andCardInHand() {
        MonsterCard m1 = putMonsterInHand(p2, "Gamma", 4, 1000, 1000);

        assertThat(p2.summonMonster(m1)).isFalse();
    }

    @Test
    void setMonster_withRequiredSacrifices() {
        MonsterCard low = putMonsterInHand(p1, "Low", 4, 1000, 1000);
        boolean lowPlaced = p1.getField().addMonsterToField(low, Mode.ATTACK, false);
        assertThat(lowPlaced).isTrue();

        MonsterCard high = putMonsterInHand(p1, "High", 5, 2000, 1600);
        ArrayList<MonsterCard> sacrifices = new ArrayList<>();
        sacrifices.add(low);
        boolean placed = p1.getField().addMonsterToField(high, Mode.DEFENSE, sacrifices);
        assertThat(placed).isTrue();

        assertThat(p1.getField().getGraveyard()).contains(low);
        assertThat(p1.getField().getMonstersArea()).contains(high);
        assertThat(high.getMode()).isEqualTo(Mode.DEFENSE);
    }

    @Test
    void addMonster_inBattlePhase_throws() {
        MonsterCard m1 = putMonsterInHand(p1, "Zeta", 4, 1000, 1000);
        p1.getField().setPhase(Phase.BATTLE);
        assertThatThrownBy(() -> p1.summonMonster(m1))
                .isInstanceOf(WrongPhaseException.class);
    }
}
