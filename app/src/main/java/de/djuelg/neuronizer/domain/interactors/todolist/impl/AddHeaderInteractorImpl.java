package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.AddHeaderInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddHeaderInteractorImpl extends AbstractInteractor implements AddHeaderInteractor {

    private final Callback callback;
    private final TodoListRepository repository;
    private final String title;
    private final int position;
    private final String parentTodoListUuid;

    public AddHeaderInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, TodoListRepository repository, String title, int position, String parentTodoListUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
        this.position = position;
        this.parentTodoListUuid = parentTodoListUuid;
    }

    @Override
    public void run() {
        final Optional<TodoList> todoList = repository.getTodoListById(parentTodoListUuid);
        if (!todoList.isPresent()) {
            callback.onParentNotFound();
            return;
        }

        // try to insert with new UUID on failure
        TodoListHeader header = new TodoListHeader(title, position, parentTodoListUuid);
        while(!repository.insert(header)) {
            header = new TodoListHeader(title, position, parentTodoListUuid);
        }

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onHeaderAdded();
            }
        });
    }
}
