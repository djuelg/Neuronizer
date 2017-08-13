package de.djuelg.neuronizer.presentation.presenters.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.AddHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.AddHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.exception.ParentNotFoundException;
import de.djuelg.neuronizer.presentation.presenters.HeaderPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class HeaderPresenterImpl extends AbstractPresenter implements HeaderPresenter, AddHeaderInteractor.Callback, EditHeaderInteractor.Callback {

    private View mView;
    private TodoListRepository mTodoListRepository;

    public HeaderPresenterImpl(Executor executor, MainThread mainThread,
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
    public void onHeaderAdded() {
        mView.onHeaderAdded();
    }

    @Override
    public void onHeaderUpdated(TodoListHeader updatedHeader) {
        mView.onHeaderEdited();
    }

    @Override
    public void onParentNotFound() {
        throw new ParentNotFoundException("Cannot update header without parent");
    }

    @Override
    public void addHeader(String title, String parentTodoListUuid) {
        // initialize the interactor
        AddHeaderInteractor interactor = new AddHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                title,
                parentTodoListUuid
        );

        // run the interactor
        interactor.execute();
    }


    @Override
    public void editHeader(String uuid, String title, int position, boolean expanded) {
        EditHeaderInteractor interactor = new EditHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                uuid,
                title,
                position,
                expanded
        );

        interactor.execute();
    }
}
