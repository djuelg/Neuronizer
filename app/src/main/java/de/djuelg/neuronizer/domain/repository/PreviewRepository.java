package de.djuelg.neuronizer.domain.repository;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.Preview;

/**
 * A repository that is responsible for the landingpage with previews
 */
public interface PreviewRepository {
    Iterable<Preview> getAll(ItemsPerPreview itemsPerPreview);

    int count();
}
