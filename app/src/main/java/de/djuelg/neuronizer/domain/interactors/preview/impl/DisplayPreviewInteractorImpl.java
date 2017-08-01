package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayPreviewInteractorImpl extends AbstractInteractor implements DisplayPreviewInteractor {

    private final DisplayPreviewInteractor.Callback callback;
    private final PreviewRepository repository;

    public DisplayPreviewInteractorImpl(Executor threadExecutor,
                                        MainThread mainThread,
                                        Callback callback, PreviewRepository repository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
    }

    private void postPreviews(final Iterable<TodoListPreview> previews) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onPreviewsRetrieved(previews);
            }
        });
    }

    @Override
    public void run() {
        Iterable<TodoListPreview> previews = repository.getPreviews(new ItemsPerPreview(2));
        postPreviews(previews);
    }
}
