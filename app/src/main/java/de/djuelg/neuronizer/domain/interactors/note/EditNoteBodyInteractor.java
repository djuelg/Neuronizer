package de.djuelg.neuronizer.domain.interactors.note;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.preview.Note;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditNoteBodyInteractor extends Interactor {
    interface Callback {
        void onNoteUpdated(Note updatedNote);
    }
}