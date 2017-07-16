package de.djuelg.neuronizer.domain.model;


import java.util.Objects;

/**
 * Created by djuelg on 12.07.17.
 *
 * Class to avoid messing with item count in preview
 */

public class ItemsPerPreview {

    private final int count;

    public ItemsPerPreview(int count) {
        if(count < 0 ) throw new IllegalArgumentException("Count must be equal or greater than 0!");
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public boolean areZero() {
        return (count == 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ItemsPerPreview that = (ItemsPerPreview) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }

    @Override
    public String toString() {
        return "ItemsPerPreview{" +
                "count=" + count +
                '}';
    }
}
