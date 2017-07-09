package de.djuelg.neuronizer.domain.interactors.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.DisplayAllListsInteractor;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * This is an interactor boilerplate with a reference to a model repository.
 * <p/>
 */
public class DisplayAllListsInteractorImpl extends AbstractInteractor implements DisplayAllListsInteractor {

    private DisplayAllListsInteractor.Callback mCallback;
    private TodoListRepository mTodoListRepository;

    public DisplayAllListsInteractorImpl(Executor threadExecutor,
                                         MainThread mainThread,
                                         Callback callback, TodoListRepository todoListRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mTodoListRepository = todoListRepository;
    }

    private void notifyError() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                // TODO this should not be hardcoded
                mCallback.onRetrievalFailed("Create a new list!");
            }
        });
    }

    private void postAllLists(final Iterable<TodoList> msg) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onAllListsRetrieved(msg);
            }
        });
    }

    @Override
    public void run() {
        final Iterable<TodoList> lists = mTodoListRepository.getAllLists();

        if (lists == null || !lists.iterator().hasNext()) {
            notifyError();
            return;
        }

        postAllLists(lists);
    }
}
