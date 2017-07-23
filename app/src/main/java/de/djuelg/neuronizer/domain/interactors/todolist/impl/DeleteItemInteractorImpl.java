package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DeleteItemInteractor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteItemInteractorImpl extends AbstractInteractor implements DeleteItemInteractor{

    private final Callback callback;
    private final TodoListRepository repository;
    private final String uuid;

    public DeleteItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final TodoListItem deletedItem = repository.getItemById(uuid);
        if (deletedItem != null) repository.delete(deletedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemDeleted(deletedItem);
            }
        });
    }
}
