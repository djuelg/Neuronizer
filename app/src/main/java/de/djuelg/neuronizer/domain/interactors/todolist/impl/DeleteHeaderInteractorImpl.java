package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DeleteHeaderInteractor;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteHeaderInteractorImpl extends AbstractInteractor implements DeleteHeaderInteractor {

    private final Callback callback;
    private final TodoListRepository repository;
    private final String uuid;

    public DeleteHeaderInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final TodoListHeader deletedItem = repository.getHeaderById(uuid);
        if (deletedItem != null) repository.delete(deletedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onHeaderDeleted(deletedItem);
            }
        });
    }
}
