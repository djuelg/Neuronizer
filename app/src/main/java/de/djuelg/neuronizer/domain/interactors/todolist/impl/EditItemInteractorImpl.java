package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import java.util.InputMismatchException;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditItemInteractor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditItemInteractorImpl extends AbstractInteractor implements EditItemInteractor {

    private final Callback callback;
    private final TodoListRepository repository;
    private final String uuid;
    private final String title;
    private final int position;
    private final boolean important;
    private final String details;
    private final boolean done;
    private final String parentHeaderUuid;

    public EditItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String uuid, String title, int position, boolean important, String details, boolean done, String parentHeaderUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
        this.important = important;
        this.details = details;
        this.done = done;
        this.parentHeaderUuid = parentHeaderUuid;
    }

    @Override
    public void run() {
        final Optional<TodoListHeader> header = repository.getHeaderById(parentHeaderUuid);
        final Optional<TodoListItem> outDatedItem = repository.getItemById(uuid);
        if (!header.isPresent() || !outDatedItem.isPresent()) {
            throw new InputMismatchException("Header, or Item were not existing!");
        }

        final TodoListItem updatedItem =
                outDatedItem.get().update(title, position, important, details, done, parentHeaderUuid);
        repository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemUpdated(updatedItem);
            }
        });
    }
}
