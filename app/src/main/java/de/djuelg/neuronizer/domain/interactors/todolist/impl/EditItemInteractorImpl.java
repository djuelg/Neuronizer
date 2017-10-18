package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import java.util.InputMismatchException;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditItemInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.Repository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditItemInteractorImpl extends AbstractInteractor implements EditItemInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;
    private final String title;
    private final int position;
    private final boolean important;
    private final String details;
    private final boolean done;
    private final String parentHeaderUuid;

    public EditItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid, String title, int position, boolean important, String details, boolean done, String parentHeaderUuid) {
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
        final Optional<TodoListHeader> header = repository.todoList().getHeaderById(parentHeaderUuid);
        final Optional<TodoListItem> outDatedItem = repository.todoList().getItemById(uuid);
        if (!header.isPresent() || !outDatedItem.isPresent()) {
            throw new InputMismatchException("Header, or Item were not existing!");
        }

        final TodoListItem updatedItem =
                outDatedItem.get().update(title, position, important, details, done, parentHeaderUuid);
        repository.todoList().update(updatedItem);

        final Optional<TodoList> todoList = repository.todoList().getTodoListById(updatedItem.getParentTodoListUuid());
        final TodoListItem itemFromUI = new TodoListItem(uuid, title,outDatedItem.get().getCreatedAt(), outDatedItem.get().getChangedAt(),
                position, important, details, done, outDatedItem.get().getParentTodoListUuid(), parentHeaderUuid);
        if (todoList.isPresent() && !outDatedItem.get().equals(itemFromUI)) repository.todoList().update(todoList.get().updateLastChange());

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemUpdated(updatedItem);
            }
        });
    }
}
