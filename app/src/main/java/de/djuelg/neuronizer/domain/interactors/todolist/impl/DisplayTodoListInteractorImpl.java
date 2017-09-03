package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.collections.Lists;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayTodoListInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayTodoListInteractorImpl extends AbstractInteractor implements DisplayTodoListInteractor {

    private static final int ACCESS_PEAK = 180;

    private final Callback callback;
    private final TodoListRepository repository;
    private final String uuid;

    public DisplayTodoListInteractorImpl(Executor threadExecutor,
                                         MainThread mainThread,
                                         Callback callback, TodoListRepository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    private void postTodoList(final Iterable<TodoListSection> sections) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onTodoListRetrieved(Lists.newArrayList(sections));
            }
        });
    }

    @Override
    public void run() {
        Optional<TodoList> todoList = repository.getTodoListById(uuid);
        if (todoList.isPresent()) {
            Iterable<TodoListSection> sections = repository.getSectionsOfTodoListId(uuid);
            postTodoList(sections);

            final TodoList loadedTodoList = todoList.get().increaseAccessCounter();
            repository.update(loadedTodoList);
            if (loadedTodoList.getAccessCounter() >= ACCESS_PEAK) normalizeImportance();
        }
    }

    private void normalizeImportance() {
        List<TodoList> todoLists = repository.getTodoLists();

        for (TodoList todoList : todoLists) {
            repository.update(todoList.normalizeAccessCounter());
        }
    }
}
