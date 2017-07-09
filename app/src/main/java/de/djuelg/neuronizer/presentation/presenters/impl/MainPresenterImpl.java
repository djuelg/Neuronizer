package de.djuelg.neuronizer.presentation.presenters.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.DisplayAllListsInteractor;
import de.djuelg.neuronizer.domain.interactors.impl.DisplayAllListsInteractorImpl;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.presenters.MainPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by dmilicic on 12/13/15.
 */
public class MainPresenterImpl extends AbstractPresenter implements MainPresenter,
        DisplayAllListsInteractor.Callback {

    private MainPresenter.View mView;
    private TodoListRepository mTodoListRepository;

    public MainPresenterImpl(Executor executor, MainThread mainThread,
                             View view, TodoListRepository todoListRepository) {
        super(executor, mainThread);
        mView = view;
        mTodoListRepository = todoListRepository;
    }

    @Override
    public void resume() {

        mView.showProgress();

        // initialize the interactor
        DisplayAllListsInteractor interactor = new DisplayAllListsInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository
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
    public void onError(String message) {
        mView.showError(message);
    }

    @Override
    public void onAllListsRetrieved(Iterable<TodoList> lists) {
        mView.hideProgress();
        mView.displayAllLists(lists);
    }

    @Override
    public void onRetrievalFailed(String error) {
        mView.hideProgress();
        onError(error);
    }
}
