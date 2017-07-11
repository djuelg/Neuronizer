package de.djuelg.neuronizer.domain.interactors.preview.impl;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.base.AbstractInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.DeleteTodoListInteractor;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteTodoListInteractorImpl extends AbstractInteractor implements DeleteTodoListInteractor {

    // TODO rename all this mShit to shit. Everywhere...
    private final DeleteTodoListInteractor.Callback mCallback;
    private final PreviewRepository mPreviewRepository;

    private final String mUuid;

    public DeleteTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback mCallback, PreviewRepository mPreviewRepository, String mUuid) {
        super(threadExecutor, mainThread);
        this.mCallback = mCallback;
        this.mPreviewRepository = mPreviewRepository;
        this.mUuid = mUuid;
    }

    @Override
    public void run() {
        final TodoList deletedItem = mPreviewRepository.getTodoListById(mUuid);
        if (deletedItem != null) mPreviewRepository.delete(deletedItem);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTodoListDeleted(deletedItem);
            }
        });
    }
}
