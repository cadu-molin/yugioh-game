package eg.edu.guc.yugioh.exceptions;

public class GameException extends RuntimeException {
    public enum Type {
        MULTIPLE_MONSTER_ADDITION("Multiple monster addition is illegal."),
        MONSTER_MULTIPLE_ATTACK("Attacking twice with the same monster is illegal."),
        DEFENSE_MONSTER_ATTACK("Defence monsters can't attack."),
        WRONG_PHASE("This action is illegal in this phase."),
        NO_MONSTER_SPACE("Monsters Area is full."),
        NO_SPELL_SPACE("Spells Area is full.");

        private final String message;
        Type(String message) { this.message = message; }
        public String getMessage() { return message; }
    }

    private final Type type;

    public GameException(Type type) {
        super(type.getMessage());
        this.type = type;
    }
}
