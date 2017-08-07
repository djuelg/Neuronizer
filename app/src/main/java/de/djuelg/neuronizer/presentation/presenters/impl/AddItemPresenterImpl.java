package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.Collections;
import java.util.List;

import de.djuelg.neuronizer.domain.comparator.PositionComparator;
import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.AddItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayHeadersInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.AddItemInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DisplayHeadersInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.exception.ParentNotFoundException;
import de.djuelg.neuronizer.presentation.presenters.AddItemPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class AddItemPresenterImpl extends AbstractPresenter implements AddItemPresenter, AddItemInteractor.Callback, DisplayHeadersInteractor.Callback, EditHeaderInteractor.Callback {

    private static final boolean EXPANDED = true;

    private View mView;
    private TodoListRepository mTodoListRepository;
    private int taskCount;

    public AddItemPresenterImpl(Executor executor, MainThread mainThread,
                                View view, TodoListRepository todoListRepository) {
        super(executor, mainThread);
        mView = view;
        mTodoListRepository = todoListRepository;
        taskCount = 0;
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
        if (taskCount == 1) {
            mView.itemAdded();
        } else {
            taskCount++;
        }
    }

    @Override
    public void onHeaderUpdated(TodoListHeader updatedHeader) {
        if (taskCount == 1) {
            mView.itemAdded();
        } else {
            taskCount++;
        }
    }

    @Override
    public void onHeaderRetrieved(List<TodoListHeader> headers) {
        Collections.sort(headers, new PositionComparator());
        mView.onHeadersLoaded(headers);
    }

    @Override
    public void onParentNotFound() {
        throw new ParentNotFoundException("Cannot add item without parent");
    }

    @Override
    public void addItem(String title, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        // initialize the interactor
        AddItemInteractor interactor = new AddItemInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                title,
                important,
                details,
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

    @Override
    public void expandHeaderOfItem(String uuid, String title, int position) {
        EditHeaderInteractor interactor = new EditHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                uuid,
                title,
                position,
                EXPANDED
        );

        interactor.execute();
    }
}
