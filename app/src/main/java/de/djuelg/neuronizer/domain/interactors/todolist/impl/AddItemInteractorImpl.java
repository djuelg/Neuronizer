package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import java.util.Date;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.AddItemInteractor;
import de.djuelg.neuronizer.domain.model.Deadline;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddItemInteractorImpl extends AbstractInteractor implements AddItemInteractor {

    private final Callback callback;
    private final TodoListRepository repository;
    private final String title;
    private final int position;
    private final Date deadline;
    private final boolean important;
    private final String details;
    private final String parentTodoListUuid;
    private final String parentHeaderUuid;

    public AddItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String title, int position, Date deadline, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
        this.position = position;
        this.deadline = deadline;
        this.important = important;
        this.details = details;
        this.parentTodoListUuid = parentTodoListUuid;
        this.parentHeaderUuid = parentHeaderUuid;
    }

    @Override
    public void run() {
        // try to insert with new UUID on failure
        TodoListItem item = new TodoListItem(title, position, new Deadline(deadline), important, details, parentTodoListUuid, parentHeaderUuid);
        while(!repository.insert(item)) {
            item = new TodoListItem(title, position, new Deadline(deadline), important, details, parentTodoListUuid, parentHeaderUuid);
        }

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemAdded();
            }
        });
    }
}
