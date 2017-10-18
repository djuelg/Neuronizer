package de.djuelg.neuronizer.domain.interactors.note;


import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.preview.Note;


public interface DisplayNoteInteractor extends Interactor {

    interface Callback {
        void onNoteRetrieved(Optional<Note> note);
    }
}
