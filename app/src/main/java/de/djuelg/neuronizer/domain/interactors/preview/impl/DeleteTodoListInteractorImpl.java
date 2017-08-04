package de.djuelg.neuronizer.domain.interactors.preview.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.DeleteTodoListInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteTodoListInteractorImpl extends AbstractInteractor implements DeleteTodoListInteractor {

    private final DeleteTodoListInteractor.Callback callback;
    private final PreviewRepository repository;
    private final String uuid;

    public DeleteTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, PreviewRepository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final Optional<TodoList> deletedItem = repository.getTodoListById(uuid);
        if (deletedItem.isPresent()) {
            repository.delete(deletedItem.get());

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onTodoListDeleted(deletedItem.get());
                }
            });
        }
    }
}
