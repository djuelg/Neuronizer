package de.djuelg.neuronizer.presentation.presenters.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.AddNoteInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.RenameNoteInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.impl.AddNoteInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.preview.impl.RenameNoteInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.Repository;
import de.djuelg.neuronizer.presentation.presenters.NotePresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class NotePresenterImpl extends AbstractPresenter implements NotePresenter, AddNoteInteractor.Callback, RenameNoteInteractor.Callback {

    private View mView;
    private Repository repository;

    public NotePresenterImpl(Executor executor, MainThread mainThread,
                             View view, Repository previewRepository) {
        super(executor, mainThread);
        mView = view;
        repository = previewRepository;
    }

    @Override
    public void resume() {
        // Nothing to do
    }

    @Override
    public void pause() {
        // Nothing to do
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    @Override
    public void addNote(String title) {
        AddNoteInteractor interactor = new AddNoteInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                title);

        interactor.execute();
    }

    @Override
    public void editNote(String uuid, String title, int position) {
        RenameNoteInteractor interactor = new RenameNoteInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid,
                title,
                position
        );

        interactor.execute();
    }

    @Override
    public void onNoteAdded(String uuid, String title) {
        mView.onNoteAdded(uuid, title);
    }

    @Override
    public void onNoteUpdated(Note updatedNote) {
        mView.onNoteEdited(updatedNote.getUuid(), updatedNote.getTitle());
    }
}
