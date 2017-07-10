package de.djuelg.neuronizer.domain.interactors.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.AddTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddTodoListInteractorImpl extends AbstractInteractor implements AddTodoListInteractor {

    private final AddTodoListInteractorImpl.Callback mCallback;
    private final TodoListRepository mTodoListRepository;

    private final TodoList mTodoList;

    public AddTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback mCallback, TodoListRepository mTodoListRepository, TodoList mTodoList) {
        super(threadExecutor, mainThread);
        this.mCallback = mCallback;
        this.mTodoListRepository = mTodoListRepository;
        this.mTodoList = mTodoList;
    }

    @Override
    public void run() {
        // try to insert with new UUID on failure
        TodoList item = mTodoList;
        while(!mTodoListRepository.insert(item)) {
            item = mTodoList.newInstance();
        }

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTodoListAdded();
            }
        });
    }
}
