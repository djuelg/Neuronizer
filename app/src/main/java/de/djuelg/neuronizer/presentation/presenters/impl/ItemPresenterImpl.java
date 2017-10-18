package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.Collections;
import java.util.List;

import de.djuelg.neuronizer.domain.comparator.PositionComparator;
import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.AddItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayHeadersInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.AddItemInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DisplayHeadersInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DisplayItemInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditItemInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.Repository;
import de.djuelg.neuronizer.presentation.exception.ParentNotFoundException;
import de.djuelg.neuronizer.presentation.presenters.ItemPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class ItemPresenterImpl extends AbstractPresenter implements ItemPresenter, AddItemInteractor.Callback, DisplayHeadersInteractor.Callback, EditHeaderInteractor.Callback, DisplayItemInteractor.Callback, EditItemInteractor.Callback {

    private static final boolean EXPANDED = true;

    private View mView;
    private Repository repository;
    private int taskCount;

    public ItemPresenterImpl(Executor executor, MainThread mainThread,
                             View view, Repository repository) {
        super(executor, mainThread);
        mView = view;
        this.repository = repository;
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
            mView.itemSynced();
        } else {
            taskCount++;
        }
    }

    @Override
    public void onHeaderUpdated(TodoListHeader updatedHeader) {
        if (taskCount == 1) {
            mView.itemSynced();
        } else {
            taskCount++;
        }
    }

    @Override
    public void onItemUpdated(TodoListItem updatedItem) {
        mView.itemSynced();
    }
    
    @Override
    public void onHeadersRetrieved(List<TodoListHeader> headers) {
        Collections.sort(headers, new PositionComparator());
        mView.onHeadersLoaded(headers);
    }

    @Override
    public void onItemRetrieved(TodoListItem item) {
        mView.onItemLoaded(item);
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
                repository,
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
    public void editItem(String uuid, String title, int position, boolean important, String details, boolean done, String parentTodoListUuid, String parentHeaderUuid) {
        // initialize the interactor
        EditItemInteractor interactor = new EditItemInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid,
                title,
                position,
                important,
                details,
                done,
                parentHeaderUuid
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void addMode(String todoListUuid) {
        DisplayHeadersInteractor interactor = new DisplayHeadersInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                todoListUuid
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void editMode(String itemUuid) {
        DisplayItemInteractor interactor = new DisplayItemInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                itemUuid
        );

        interactor.execute();
    }

    @Override
    public void expandHeaderOfItem(String uuid, String title, int position) {
        EditHeaderInteractor interactor = new EditHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid,
                title,
                position,
                EXPANDED
        );

        interactor.execute();
    }
}
