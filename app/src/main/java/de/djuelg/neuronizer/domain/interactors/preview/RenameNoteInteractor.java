package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.preview.Note;

/**
 * Created by djuelg on 10.07.17.
 */

public interface RenameNoteInteractor extends Interactor {
    interface Callback {
        void onNoteUpdated(Note updatedNote);
    }
}