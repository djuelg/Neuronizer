package de.djuelg.neuronizer.presentation.presenters;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface DisplayNotePresenter extends BasePresenter {

    void loadNote(String uuid);

    void editNote(String uuid, String body);

    interface View {

        void onNoteLoaded(Optional<Note> note);
    }
}
