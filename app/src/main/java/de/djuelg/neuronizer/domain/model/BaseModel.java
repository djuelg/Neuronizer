package de.djuelg.neuronizer.domain.model;

import java.util.Date;

/**
 * Created by djuelg on 09.07.17.
 *
 * This interface contains all mandatory fields for any todolist item
 */

public interface BaseModel {
    String getUuid();

    String getTitle();

    Date getCreatedAt();

    Date getChangedAt();

    int getPosition();
}
