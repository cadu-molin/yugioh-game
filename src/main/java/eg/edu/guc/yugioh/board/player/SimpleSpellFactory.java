package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.cards.spells.*;
import eg.edu.guc.yugioh.exceptions.UnknownSpellCardException;

public class SimpleSpellFactory extends SpellFactory {
    public static SpellCard createSpell(String name, String description, String path, Integer lineNumber) throws UnknownSpellCardException {
        return switch (name) {
            case "Card Destruction" -> new CardDestruction(name, description);
            case "Change Of Heart" -> new ChangeOfHeart(name, description);
            case "Dark Hole" -> new DarkHole(name, description);
            case "Graceful Dice" -> new GracefulDice(name, description);
            case "Harpie's Feather Duster" -> new HarpieFeatherDuster(name, description);
            case "Heavy Storm" -> new HeavyStorm(name, description);
            case "Mage Power" -> new MagePower(name, description);
            case "Monster Reborn" -> new MonsterReborn(name, description);
            case "Pot of Greed" -> new PotOfGreed(name, description);
            case "Raigeki" -> new Raigeki(name, description);
            default -> throw new UnknownSpellCardException("Unknown Spell card", path, lineNumber, name);
        };
    }
}