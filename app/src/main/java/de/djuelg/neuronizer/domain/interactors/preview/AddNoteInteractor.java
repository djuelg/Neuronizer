package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddNoteInteractor extends Interactor {
    interface Callback {
        void onNoteAdded(String uuid, String title);
    }
}
