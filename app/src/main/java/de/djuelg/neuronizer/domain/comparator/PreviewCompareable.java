package de.djuelg.neuronizer.domain.comparator;

import java.util.Date;

/**
 * Created by Domi on 06.08.2017.
 */

public interface PreviewCompareable {

    long getAccessCounter();

    Date getChangedAt();

    Date getCreatedAt();

    String getTitle();
}
