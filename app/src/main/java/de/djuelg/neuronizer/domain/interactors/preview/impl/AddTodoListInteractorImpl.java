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

    private final AddTodoListInteractorImpl.Callback mCallback;
    private final PreviewRepository mPreviewRepository;

    private final String mTitle;
    private final int mPosition;

    public AddTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback mCallback, PreviewRepository mPreviewRepository, String mTitle, int mPosition) {
        super(threadExecutor, mainThread);
        this.mCallback = mCallback;
        this.mPreviewRepository = mPreviewRepository;
        this.mTitle = mTitle;
        this.mPosition = mPosition;
    }

    @Override
    public void run() {
        // try to insert with new UUID on failure
        TodoList item = new TodoList(mTitle, mPosition);
        while(!mPreviewRepository.insert(item)) {
            item = item.newInstance();
        }

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTodoListAdded();
            }
        });
    }
}
