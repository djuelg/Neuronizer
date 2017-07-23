package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DisplayPreviewInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.presentation.presenters.PreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewUI;

/**
 * Created by dmilicic on 12/13/15.
 */
public class PreviewPresenterImpl extends AbstractPresenter implements PreviewPresenter,
        DisplayPreviewInteractor.Callback {

    private PreviewPresenter.View mView;
    private PreviewRepository mPreviewRepository;

    public PreviewPresenterImpl(Executor executor, MainThread mainThread,
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
        List<TodoListPreviewUI> previewUIs = new ArrayList<>();
        for (TodoListPreview preview : previews) {
            previewUIs.add(new TodoListPreviewUI(preview));
        }
        mView.displayPreviews(previewUIs);
    }
}
