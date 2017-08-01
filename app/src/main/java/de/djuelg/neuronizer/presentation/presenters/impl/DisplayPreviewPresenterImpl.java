package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DisplayPreviewInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.presentation.presenters.DisplayPreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewViewModel;

/**
 * Created by dmilicic on 12/13/15.
 */
public class DisplayPreviewPresenterImpl extends AbstractPresenter implements DisplayPreviewPresenter,
        DisplayPreviewInteractor.Callback {

    private DisplayPreviewPresenter.View mView;
    private PreviewRepository mPreviewRepository;

    public DisplayPreviewPresenterImpl(Executor executor, MainThread mainThread,
                                       View view, PreviewRepository previewRepository) {
        super(executor, mainThread);
        mView = view;
        mPreviewRepository = previewRepository;
    }

    @Override
    public void resume() {
        // initialize the interactor
        DisplayPreviewInteractor interactor = new DisplayPreviewInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mPreviewRepository
        );

        // run the interactor
        interactor.execute();
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
    public void onPreviewsRetrieved(Iterable<TodoListPreview> previews) {
        List<TodoListPreviewViewModel> previewVMs = new ArrayList<>();
        for (TodoListPreview preview : previews) {
            previewVMs.add(new TodoListPreviewViewModel(preview));
        }
        mView.onPreviewsLoaded(previewVMs);
    }
}
