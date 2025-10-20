package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.exceptions.UnknownSpellCardException;

public abstract class SpellFactory {
    public static SpellCard createSpell(String name, String description, String path, Integer lineNumber) throws UnknownSpellCardException {
        return null;
    }
}