package de.djuelg.neuronizer.presentation.presenters.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DisplayTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditItemInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.presenters.DisplayTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListItemViewModel;

import static de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel.headerComparator;
import static de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListItemViewModel.itemComparator;

/**
 * Created by dmilicic on 12/13/15.
 */
public class DisplayTodoListPresenterImpl extends AbstractPresenter implements DisplayTodoListPresenter,
        DisplayTodoListInteractor.Callback, EditHeaderInteractor.Callback, EditItemInteractor.Callback {

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
        List<TodoListHeaderViewModel> headerVMs = new ArrayList<>(sections.size());

        for (TodoListSection section : sections) {
            TodoListHeaderViewModel headerVM = new TodoListHeaderViewModel(section.getHeader());
            headerVM.setSubItems(createSubItemList(headerVM, section.getItems()));
            headerVMs.add(headerVM);
        }
        Collections.sort(headerVMs, headerComparator());
        mView.onTodoListLoaded(headerVMs);
    }

    private List<TodoListItemViewModel> createSubItemList(TodoListHeaderViewModel headerVM, Iterable<TodoListItem> items) {
        List<TodoListItemViewModel> itemVMs = new ArrayList<>();
        for (TodoListItem item : items) {
            itemVMs.add(new TodoListItemViewModel(headerVM, item));
        }
        Collections.sort(itemVMs, itemComparator());
        return itemVMs;
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

    @Override
    public void syncTodoList(List<TodoListHeaderViewModel> headerItems) {
        for (TodoListHeaderViewModel vm : headerItems) {
            TodoListHeader header = vm.getHeader();
            syncHeader(header, headerItems.indexOf(vm), vm.isExpanded());
            syncSubItems(vm.getSubItems());
        }
    }

    private void syncHeader(TodoListHeader header, int vmPosition, boolean vmExpanded) {
        EditHeaderInteractor interactor = new EditHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                header.getUuid(),
                header.getTitle(),
                vmPosition,
                vmExpanded
        );

        interactor.execute();
    }

    private void syncSubItems(List<TodoListItemViewModel> subItems) {
        for (TodoListItemViewModel vm : subItems) {
            TodoListItem item = vm.getItem();

            EditItemInteractor interactor = new EditItemInteractorImpl(
                    mExecutor,
                    mMainThread,
                    this,
                    mTodoListRepository,
                    item.getUuid(),
                    item.getTitle(),
                    subItems.indexOf(vm),
                    item.isImportant(),
                    item.getDetails(),
                    item.isDone(),
                    vm.getHeader().getHeader().getUuid()
            );

            interactor.execute();
        }
    }

    @Override
    public void onHeaderUpdated(TodoListHeader updatedHeader) {
        // nothing to to
    }

    @Override
    public void onItemUpdated(TodoListItem updatedItem) {
        // nothing to to
    }
}
