package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.model.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayPreviewInteractorImpl extends AbstractInteractor implements DisplayPreviewInteractor {

    private final DisplayPreviewInteractor.Callback callback;
    private final PreviewRepository previewRepository;

    public DisplayPreviewInteractorImpl(Executor threadExecutor,
                                        MainThread mainThread,
                                        Callback callback, PreviewRepository previewRepository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.previewRepository = previewRepository;
    }

    private void notifyError() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onRetrievalFailed(ExceptionId.NO_LISTS);
            }
        });
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
        final Iterable<TodoListPreview> previews = previewRepository.getPreviews(new ItemsPerPreview(2));

        if (previews == null || !previews.iterator().hasNext()) {
            notifyError();
            return;
        }

        postPreviews(previews);
    }
}
