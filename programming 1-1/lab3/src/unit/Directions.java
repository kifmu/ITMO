package unit;

public class Directions {
    public enum Prepositions {
        ON,
        IN,
        UNDER, 
        NONE
    }

    public enum Type {
        WITH,
        AT,
        TO,
        NONE
    }

    private final Type type;
    private final Prepositions preposition;
    private final String text;

    public Directions(Type type, Prepositions preposition, String text) {
        this.type = type;
        this.preposition = preposition;
        this.text = text;
    }

    private String decodePrep() {
        return switch (preposition) {
            case ON -> "на";
            case IN -> "в";
            case UNDER -> "над";
            case NONE -> "";
        };

    }

    private String decodeType() {
        return switch (type) {
            case WITH -> "с";
            case AT -> "у";
            case TO -> "на";
            case NONE -> "";
        };
    }

    public String getFullDirection() {
        return " " + decodeType() + " " + decodePrep() + " " + text;
    }
}
