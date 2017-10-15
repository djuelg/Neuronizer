package de.djuelg.neuronizer.domain.model.preview;

import de.djuelg.neuronizer.domain.comparator.PositionCompareable;
import de.djuelg.neuronizer.domain.model.BaseModel;

/**
 * Created by djuelg on 15.10.17.
 */

public interface Preview extends PositionCompareable {

    BaseModel getPreview();

    String getSubtitle();

    String getDetails();

    long calculateImportance();

    @Override
    int getPosition();
}
