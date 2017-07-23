package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DisplayTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.presenters.DisplayTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by dmilicic on 12/13/15.
 */
public class DisplayTodoListPresenterImpl extends AbstractPresenter implements DisplayTodoListPresenter,
        DisplayTodoListInteractor.Callback {

    private View mView;
    private TodoListRepository mTodoListRepository;

    public DisplayTodoListPresenterImpl(Executor executor, MainThread mainThread,
                                        View view, TodoListRepository todoListRepository) {
        super(executor, mainThread);
        mView = view;
        mTodoListRepository = todoListRepository;
    }

    @Override
    public void resume() {
        // Nothing to do
    }

    @Override
    public void pause() {
        // Nothing to do
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    @Override
    public void onTodoListRetrieved(List<TodoListSection> sections) {
        List<AbstractFlexibleItem> itemVMs = new ArrayList<>(sections.size());

        for (TodoListSection section : sections) {
            TodoListHeaderViewModel headerVM = new TodoListHeaderViewModel(section.getHeader());

        }
    }

    @Override
    public void onRetrievalFailed() {
        mView.onRetrievalFailed();
    }

    @Override
    public void loadTodoList(String uuid) {
        // initialize the interactor
        DisplayTodoListInteractor interactor = new DisplayTodoListInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                uuid
        );

        // run the interactor
        interactor.execute();
    }
}
