package de.djuelg.neuronizer.domain.interactors.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.EditTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditTodoListInteractorImpl extends AbstractInteractor implements EditTodoListInteractor {

    private final EditTodoListInteractor.Callback mCallback;
    private final TodoListRepository mTodoListRepository;

    private final String mUuid;
    private final String mTitle;
    private final int mPosition;

    public EditTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback mCallback, TodoListRepository mTodoListRepository, String mUuid, String mTitle, int mPosition) {
        super(threadExecutor, mainThread);
        this.mCallback = mCallback;
        this.mTodoListRepository = mTodoListRepository;
        this.mUuid = mUuid;
        this.mTitle = mTitle;
        this.mPosition = mPosition;
    }

    @Override
    public void run() {
        final TodoList outDatedItem = mTodoListRepository.getTodoListById(mUuid);
        final TodoList updatedItem = (outDatedItem != null)
                ? outDatedItem.update(mTitle, mPosition)
                : new TodoList(mTitle, mPosition);


        mTodoListRepository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTodoListUpdated(updatedItem);
            }
        });
    }
}
