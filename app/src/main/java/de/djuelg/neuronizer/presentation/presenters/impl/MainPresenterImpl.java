package de.djuelg.neuronizer.presentation.presenters.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DisplayPreviewInteractorImpl;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.presentation.presenters.MainPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by dmilicic on 12/13/15.
 */
public class MainPresenterImpl extends AbstractPresenter implements MainPresenter,
        DisplayPreviewInteractor.Callback {

    private MainPresenter.View mView;
    private PreviewRepository mPreviewRepository;

    public MainPresenterImpl(Executor executor, MainThread mainThread,
                             View view, PreviewRepository previewRepository) {
        super(executor, mainThread);
        mView = view;
        mPreviewRepository = previewRepository;
    }

    @Override
    public void resume() {

        mView.showProgress();

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

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(ExceptionId exceptionId) {
        mView.showError(exceptionId);
    }


    @Override
    public void onPreviewsRetrieved(Iterable<TodoListPreview> previews) {
        mView.hideProgress();
        mView.displayPreviews(previews);
    }

    @Override
    public void onRetrievalFailed(ExceptionId exceptionId) {
        mView.hideProgress();
        onError(exceptionId);
    }
}
