package de.djuelg.neuronizer.domain.interactors.preview;


import java.util.List;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.preview.Preview;


public interface DisplayPreviewInteractor extends Interactor {

    interface Callback {
        void onPreviewsRetrieved(List<Preview> lists);

    }
}
