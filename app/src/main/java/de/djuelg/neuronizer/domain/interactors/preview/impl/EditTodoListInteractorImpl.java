package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.EditTodoListInteractor;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditTodoListInteractorImpl extends AbstractInteractor implements EditTodoListInteractor {

    private final EditTodoListInteractor.Callback mCallback;
    private final PreviewRepository mPreviewRepository;

    private final String mUuid;
    private final String mTitle;
    private final int mPosition;

    public EditTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback mCallback, PreviewRepository mPreviewRepository, String mUuid, String mTitle, int mPosition) {
        super(threadExecutor, mainThread);
        this.mCallback = mCallback;
        this.mPreviewRepository = mPreviewRepository;
        this.mUuid = mUuid;
        this.mTitle = mTitle;
        this.mPosition = mPosition;
    }

    @Override
    public void run() {
        final TodoList outDatedItem = mPreviewRepository.getTodoListById(mUuid);
        final TodoList updatedItem = (outDatedItem != null)
                ? outDatedItem.update(mTitle, mPosition)
                : new TodoList(mTitle, mPosition);


        mPreviewRepository.update(updatedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTodoListUpdated(updatedItem);
            }
        });
    }
}
