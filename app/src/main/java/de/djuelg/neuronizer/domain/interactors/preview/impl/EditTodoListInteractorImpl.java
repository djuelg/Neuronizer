package de.djuelg.neuronizer.domain.interactors.preview.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.EditTodoListInteractor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.Repository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditTodoListInteractorImpl extends AbstractInteractor implements EditTodoListInteractor {

    private final EditTodoListInteractor.Callback callback;
    private final Repository repository;
    private final String uuid;
    private final String title;
    private final int position;

    public EditTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid, String title, int position) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
    }

    @Override
    public void run() {
        final Optional<TodoList> outDatedItem = repository.todoList().getTodoListById(uuid);
        if (outDatedItem.isPresent()) {

            final TodoList updatedItem = title.equals(outDatedItem.get().getTitle())
                    ? outDatedItem.get().update(title, position)
                    : outDatedItem.get().update(title, position).updateLastChange();
            repository.todoList().update(updatedItem);

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onTodoListUpdated(updatedItem);
                }
            });
        }
    }
}
