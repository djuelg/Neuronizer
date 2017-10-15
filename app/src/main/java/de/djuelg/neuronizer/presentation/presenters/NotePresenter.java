package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface NotePresenter extends BasePresenter {

    void addNote(String title);

    void editNote(String uuid, String title, int position);

    interface View {

        void onNoteAdded(String uuid, String title);

        void onNoteEdited(String uuid, String title);
    }
}
