package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayPreviewInteractorImpl extends AbstractInteractor implements DisplayPreviewInteractor {

    private final DisplayPreviewInteractor.Callback mCallback;
    private final PreviewRepository mPreviewRepository;

    public DisplayPreviewInteractorImpl(Executor threadExecutor,
                                        MainThread mainThread,
                                        Callback callback, PreviewRepository previewRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mPreviewRepository = previewRepository;
    }

    private void notifyError() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onRetrievalFailed(ExceptionId.NO_LISTS);
            }
        });
    }

    private void postPreviews(final Iterable<TodoListPreview> previews) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onPreviewsRetrieved(previews);
            }
        });
    }

    @Override
    public void run() {
        final Iterable<TodoListPreview> previews = mPreviewRepository.getPreviews();

        if (previews == null || !previews.iterator().hasNext()) {
            notifyError();
            return;
        }

        postPreviews(previews);
    }
}
