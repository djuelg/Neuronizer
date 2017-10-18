package de.djuelg.neuronizer.domain.model.preview;

/**
 * Created by djuelg on 18.10.17.
 */

public interface ImportanceComparable {

    long getAccessCounter();

    ImportanceComparable increaseAccessCounter();

    ImportanceComparable normalizeAccessCounter();

    long calculateImportance();
}
