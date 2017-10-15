package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.preview.Note;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteNoteInteractor extends Interactor {
    interface Callback {
        void onNoteDeleted(Note deletedNote);
    }
}
