package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.AddNoteInteractor;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddNoteInteractorImpl extends AbstractInteractor implements AddNoteInteractor {

    private final Callback callback;
    private final PreviewRepository repository;
    private final String title;

    public AddNoteInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, PreviewRepository repository, String title) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
    }

    @Override
    public void run() {
        final int position = repository.getNumberOfPreviews();
        // try to insert with new UUID on failure
        Note item = new Note(title, position);
        while(!repository.insert(item)) {
            item = new Note(title, position);
        }

        final String uuid = item.getUuid();
        final String title = item.getTitle();

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onNoteAdded(uuid, title);
            }
        });
    }
}
