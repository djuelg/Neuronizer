package de.djuelg.neuronizer.domain.interactors.note.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.note.EditNoteBodyInteractor;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.NoteRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditNoteBodyInteractorImpl extends AbstractInteractor implements EditNoteBodyInteractor {

    private final Callback callback;
    private final NoteRepository repository;
    private final String uuid;
    private final String body;

    public EditNoteBodyInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, NoteRepository repository, String uuid, String body) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.body = body;
    }

    @Override
    public void run() {
        final Optional<Note> outDatedItem = repository.getNoteById(uuid);
        if (outDatedItem.isPresent()) {

            final Note updatedItem = outDatedItem.get().update(body).updateLastChange();
            repository.update(updatedItem);

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onNoteUpdated(updatedItem);
                }
            });
        }
    }
}
