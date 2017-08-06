package de.djuelg.neuronizer.presentation.presenters.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.AddTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.impl.AddTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.presentation.presenters.AddTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class AddTodoListPresenterImpl extends AbstractPresenter implements AddTodoListPresenter, AddTodoListInteractor.Callback {

    private AddTodoListPresenter.View mView;
    private PreviewRepository mPreviewRepository;

    public AddTodoListPresenterImpl(Executor executor, MainThread mainThread,
                                View view, PreviewRepository previewRepository) {
        super(executor, mainThread);
        mView = view;
        mPreviewRepository = previewRepository;
    }

    @Override
    public void resume() {
        // Nothing to do
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
    public void addTodoList(String title) {
        // initialize the interactor
        AddTodoListInteractor interactor = new AddTodoListInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mPreviewRepository,
                title
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void onTodoListAdded(String uuid, String title) {
        mView.onTodoListAdded(uuid, title);
    }
}
