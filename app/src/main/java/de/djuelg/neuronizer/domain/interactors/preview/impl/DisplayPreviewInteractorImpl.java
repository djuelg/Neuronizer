package de.djuelg.neuronizer.domain.interactors.preview.impl;

import com.fernandocejas.arrow.collections.Lists;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.Preview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayPreviewInteractorImpl extends AbstractInteractor implements DisplayPreviewInteractor {

    private final static int MAX_DISPLAYED_ITEMS = 4;

    private final DisplayPreviewInteractor.Callback callback;
    private final PreviewRepository repository;

    public DisplayPreviewInteractorImpl(Executor threadExecutor,
                                        MainThread mainThread,
                                        Callback callback, PreviewRepository repository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
    }

    private void postPreviews(final Iterable<Preview> previews) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onPreviewsRetrieved(Lists.newArrayList(previews));
            }
        });
    }

    @Override
    public void run() {
        Iterable<Preview> previews = repository.getPreviews(new ItemsPerPreview(MAX_DISPLAYED_ITEMS));
        postPreviews(previews);
    }
}
