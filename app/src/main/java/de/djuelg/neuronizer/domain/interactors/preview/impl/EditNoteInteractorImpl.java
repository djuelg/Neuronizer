package de.djuelg.neuronizer.domain.interactors.preview.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.EditNoteInteractor;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditNoteInteractorImpl extends AbstractInteractor implements EditNoteInteractor {

    private final Callback callback;
    private final PreviewRepository repository;
    private final String uuid;
    private final String title;
    private final int position;

    public EditNoteInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, PreviewRepository repository, String uuid, String title, int position) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
    }

    @Override
    public void run() {
        final Optional<Note> outDatedItem = repository.getNoteById(uuid);
        if (outDatedItem.isPresent()) {

            final Note updatedItem = title.equals(outDatedItem.get().getTitle())
                    ? outDatedItem.get().update(title, position)
                    : outDatedItem.get().update(title, position).updateLastChange();
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
