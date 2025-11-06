package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.cards.spells.*;
import eg.edu.guc.yugioh.exceptions.UnknownSpellCardException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SimpleSpellFactoryTest {

    @Test
    void knownSpells_shouldBeCreated() throws UnknownSpellCardException {
        assertThat(SimpleSpellFactory.createSpell("Card Destruction", "d", "", 0)).isInstanceOf(CardDestruction.class);
        assertThat(SimpleSpellFactory.createSpell("Change Of Heart", "d", "", 0)).isInstanceOf(ChangeOfHeart.class);
        assertThat(SimpleSpellFactory.createSpell("Dark Hole", "d", "", 0)).isInstanceOf(DarkHole.class);
        assertThat(SimpleSpellFactory.createSpell("Graceful Dice", "d", "", 0)).isInstanceOf(GracefulDice.class);
        assertThat(SimpleSpellFactory.createSpell("Harpie's Feather Duster", "d", "", 0)).isInstanceOf(HarpieFeatherDuster.class);
        assertThat(SimpleSpellFactory.createSpell("Heavy Storm", "d", "", 0)).isInstanceOf(HeavyStorm.class);
        assertThat(SimpleSpellFactory.createSpell("Mage Power", "d", "", 0)).isInstanceOf(MagePower.class);
        assertThat(SimpleSpellFactory.createSpell("Monster Reborn", "d", "", 0)).isInstanceOf(MonsterReborn.class);
        assertThat(SimpleSpellFactory.createSpell("Pot of Greed", "d", "", 0)).isInstanceOf(PotOfGreed.class);
        assertThat(SimpleSpellFactory.createSpell("Raigeki", "d", "", 0)).isInstanceOf(Raigeki.class);
    }

    @Test
    void unknownSpell_shouldThrow() {
        assertThatThrownBy(() -> SimpleSpellFactory.createSpell("Unknown X", "d", "path.csv", 42))
                .isInstanceOf(UnknownSpellCardException.class)
                .hasMessageContaining("Unknown Spell card");
    }
}
