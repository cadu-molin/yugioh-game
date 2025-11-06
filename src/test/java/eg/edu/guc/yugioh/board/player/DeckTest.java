package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

    @Test
    void newDeck_shouldHaveExpectedSize_andDrawReduceSize() throws IOException, UnexpectedFormatException {
        Deck deck = new Deck();

        assertThat(deck.getDeck()).hasSizeGreaterThanOrEqualTo(37);

        int before = deck.getDeck().size();
        Card c1 = deck.drawOneCard();
        assertThat(deck.getDeck()).hasSize(before - 1);
        assertThat(c1).isNotNull();

        int beforeN = deck.getDeck().size();
        var list = deck.drawNCards(3);
        assertThat(list).hasSize(3);
        assertThat(deck.getDeck()).hasSize(beforeN - 3);
    }
}
