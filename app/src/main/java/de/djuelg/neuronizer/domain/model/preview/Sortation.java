package de.djuelg.neuronizer.domain.model.preview;

/**
 * Created by Domi on 22.08.2017.
 */

public enum Sortation {
    IMPORTANCE(0),
    LAST_CHANGE(1),
    CREATION_DATE(2),
    ALPHABETICAL(3);

    private final int position;

    Sortation(int position) {
        this.position = position;
    }

    public static Sortation parse(int position) {
        switch (position) {
            case 0:
                return IMPORTANCE;
            case 1:
                return LAST_CHANGE;
            case 2:
                return CREATION_DATE;
            case 3:
                return ALPHABETICAL;
            default:
                break;
        }
        throw new IllegalArgumentException(position + " is out of bounds.");
    }

    public int toInt() {
        return position;
    }
}
