package de.djuelg.neuronizer.domain.interactors.preview.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.RenameNoteInteractor;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.Repository;

/**
 * Created by djuelg on 10.07.17.
 */

public class RenameNoteInteractorImpl extends AbstractInteractor implements RenameNoteInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;
    private final String title;
    private final int position;

    public RenameNoteInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid, String title, int position) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
    }

    @Override
    public void run() {
        final Optional<Note> outDatedItem = repository.note().get(uuid);
        if (outDatedItem.isPresent()) {

            final Note updatedItem = title.equals(outDatedItem.get().getTitle())
                    ? outDatedItem.get().update(title, position)
                    : outDatedItem.get().update(title, position).updateLastChange();
            repository.note().update(updatedItem);

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onNoteUpdated(updatedItem);
                }
            });
        }
    }
}
