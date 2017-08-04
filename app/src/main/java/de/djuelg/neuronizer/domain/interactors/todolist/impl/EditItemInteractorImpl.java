package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditItemInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
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
    private final String parentTodoListUuid;
    private final String parentHeaderUuid;

    public EditItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String uuid, String title, int position, boolean important, String details, boolean done, String parentTodoListUuid, String parentHeaderUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
        this.important = important;
        this.details = details;
        this.done = done;
        this.parentTodoListUuid = parentTodoListUuid;
        this.parentHeaderUuid = parentHeaderUuid;
    }

    @Override
    public void run() {
        final TodoList todoList = repository.getTodoListById(parentTodoListUuid);
        final TodoListHeader header = repository.getHeaderById(parentHeaderUuid);
        if ( todoList == null || header == null) {
            callback.onParentNotFound();
            return;
        }

        final TodoListItem outDatedItem = repository.getItemById(uuid);
        final TodoListItem updatedItem =
                outDatedItem.update(title, position, important, details, done, parentTodoListUuid, parentHeaderUuid);
        repository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemUpdated(updatedItem);
            }
        });
    }
}
