package de.djuelg.neuronizer.domain.interactors.note.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.note.DisplayNoteInteractor;
import de.djuelg.neuronizer.domain.model.preview.Importance;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayNoteInteractorImpl extends AbstractInteractor implements DisplayNoteInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;

    public DisplayNoteInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     Callback callback, Repository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final Optional<Note> note = repository.note().get(uuid);
        if (note.isPresent()) {
            Importance.increase(repository, note.get());
            Importance.checkForNormalization(repository, note.get());
        }

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onNoteRetrieved(note);
            }
        });
    }
}
