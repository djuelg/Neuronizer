package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.Color;
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
    private final int color;
    private final String parentTodoListUuid;

    public EditHeaderInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String uuid, String title, int position, int color, String parentTodoListUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
        this.color = color;
        this.parentTodoListUuid = parentTodoListUuid;
    }

    @Override
    public void run() {
        final TodoList todoList = repository.getTodoListById(parentTodoListUuid);
        if ( todoList == null) {
            callback.onParentNotFound();
            return;
        }

        final TodoListHeader outDatedItem = repository.getHeaderById(uuid);
        final TodoListHeader updatedItem = (outDatedItem != null)
                ? outDatedItem.update(title, position, new Color(color), parentTodoListUuid)
                : new TodoListHeader(title, position, new Color(color), parentTodoListUuid);

        repository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onHeaderUpdated(updatedItem);
            }
        });
    }
}
