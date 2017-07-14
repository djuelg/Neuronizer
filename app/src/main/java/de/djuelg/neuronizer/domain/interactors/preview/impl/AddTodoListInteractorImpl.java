package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.AddTodoListInteractor;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddTodoListInteractorImpl extends AbstractInteractor implements AddTodoListInteractor {

    private final AddTodoListInteractorImpl.Callback callback;
    private final PreviewRepository repository;
    private final String title;
    private final int position;

    public AddTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, PreviewRepository repository, String title, int position) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
        this.position = position;
    }

    @Override
    public void run() {
        // try to insert with new UUID on failure
        TodoList item = new TodoList(title, position);
        while(!repository.insert(item)) {
            item = new TodoList(title, position);
        }

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onTodoListAdded();
            }
        });
    }
}
