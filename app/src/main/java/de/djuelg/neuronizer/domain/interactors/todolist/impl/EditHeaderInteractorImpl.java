package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import java.util.InputMismatchException;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditHeaderInteractorImpl extends AbstractInteractor implements EditHeaderInteractor {

    private final Callback callback;
    private final TodoListRepository repository;
    private final String uuid;
    private final String title;
    private final int position;
    private final boolean expanded;

    public EditHeaderInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String uuid, String title, int position, boolean expanded) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
        this.expanded = expanded;
    }

    @Override
    public void run() {
        final Optional<TodoListHeader> outDatedItem = repository.getHeaderById(uuid);
        if (!outDatedItem.isPresent()) {
            throw new InputMismatchException("Item not existing!");
        }

        final TodoListHeader updatedItem = outDatedItem.get().update(title, position, expanded);
        repository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onHeaderUpdated(updatedItem);
            }
        });
    }
}
