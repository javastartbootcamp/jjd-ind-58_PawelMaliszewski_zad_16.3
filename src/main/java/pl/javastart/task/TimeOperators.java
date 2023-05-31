package pl.javastart.task;

public enum TimeOperators {
    YEAR('y'),
    MONTH('M'),
    DAY('d'),
    HOUR('h'),
    MINUTES('m'),
    SECONDS('s');

    private final char description;

    TimeOperators(char description) {
        this.description = description;
    }

    public char getDescription() {
        return description;
    }

    public static TimeOperators grtFromDescription(char s) {
        for (TimeOperators value : values()) {
            if (value.description == s) {
                return value;
            }
        }
        return null;
    }
}
