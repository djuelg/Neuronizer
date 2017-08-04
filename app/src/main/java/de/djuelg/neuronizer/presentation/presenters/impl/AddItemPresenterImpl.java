package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.AddItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayHeadersInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.AddItemInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DisplayHeadersInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.exception.ParentNotFoundException;
import de.djuelg.neuronizer.presentation.presenters.AddItemPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class AddItemPresenterImpl extends AbstractPresenter implements AddItemPresenter, AddItemInteractor.Callback, DisplayHeadersInteractor.Callback {

    private View mView;
    private TodoListRepository mTodoListRepository;

    public AddItemPresenterImpl(Executor executor, MainThread mainThread,
                                View view, TodoListRepository todoListRepository) {
        super(executor, mainThread);
        mView = view;
        mTodoListRepository = todoListRepository;
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
    public void onItemAdded() {
        mView.itemAdded();
    }

    @Override
    public void onHeaderRetrieved(List<TodoListHeader> headers) {
        mView.onHeadersLoaded(headers);
    }

    @Override
    public void onParentNotFound() {
        throw new ParentNotFoundException("Cannot add item without parent");
    }

    @Override
    public void addItem(String title, boolean important, String Details, String parentTodoListUuid, String parentHeaderUuid) {
        // initialize the interactor
        AddItemInteractor interactor = new AddItemInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                title,
                0,
                true,
                "",
                parentTodoListUuid,
                parentHeaderUuid
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void getHeaders(String todoListUuid) {
        DisplayHeadersInteractor interactor = new DisplayHeadersInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                todoListUuid
        );

        // run the interactor
        interactor.execute();
    }
}
