package de.djuelg.neuronizer.domain.interactors.todolist.impl;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayTodoListInteractor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayTodoListInteractorImpl extends AbstractInteractor implements DisplayTodoListInteractor {

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

    private void postTodoList(final List<TodoListSection> sections) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onTodoListRetrieved(sections);
            }
        });
    }

    @Override
    public void run() {
        List<TodoListSection> sections = repository.getSectionsOfTodoListId(uuid);
        if (sections == null) {
            sections = new ArrayList<>(0);
        }

        postTodoList(sections);
    }
}
