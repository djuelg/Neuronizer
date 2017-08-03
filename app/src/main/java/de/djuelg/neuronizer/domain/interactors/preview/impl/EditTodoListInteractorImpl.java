package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.EditTodoListInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditTodoListInteractorImpl extends AbstractInteractor implements EditTodoListInteractor {

    private final EditTodoListInteractor.Callback callback;
    private final PreviewRepository repository;
    private final String uuid;
    private final String title;
    private final int position;

    public EditTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, PreviewRepository repository, String uuid, String title, int position) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
    }

    @Override
    public void run() {
        final TodoList outDatedItem = repository.getTodoListById(uuid);
        final TodoList updatedItem = outDatedItem.update(title, position);
        repository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onTodoListUpdated(updatedItem);
            }
        });
    }
}
