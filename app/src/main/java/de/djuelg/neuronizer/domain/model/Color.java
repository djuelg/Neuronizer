package de.djuelg.neuronizer.domain.model;

import java.util.Objects;

/**
 * Created by Domi on 26.03.2017.
 *
 * Convenience model for using colors
 */

public class Color {

    private final int color;

    public Color(int color) {
        this.color = Objects.requireNonNull(color);
    }

    /**
     * @return ColorInt value
     */
    public int toInt() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color1 = (Color) o;
        return color == color1.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public String toString() {
        return "Color{" +
                "color=" + color +
                '}';
    }
}