package de.djuelg.neuronizer.presentation.presenters.impl;

import com.fernandocejas.arrow.collections.Lists;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.djuelg.neuronizer.domain.comparator.PositionComparator;
import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.DeleteHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DeleteItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.DisplayTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.EditItemInteractor;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DeleteHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DeleteItemInteractorImpl;
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
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;

/**
 * Created by dmilicic on 12/13/15.
 */
public class DisplayTodoListPresenterImpl extends AbstractPresenter implements DisplayTodoListPresenter,
        DisplayTodoListInteractor.Callback, EditHeaderInteractor.Callback, EditItemInteractor.Callback, DeleteItemInteractor.Callback, DeleteHeaderInteractor.Callback {

    private View mView;
    private TodoListRepository mTodoListRepository;
    private boolean reload;

    public DisplayTodoListPresenterImpl(Executor executor, MainThread mainThread,
                                        View view, TodoListRepository todoListRepository) {
        super(executor, mainThread);
        mView = view;
        mTodoListRepository = todoListRepository;
        reload = false;
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
        Collections.sort(sections, new PositionComparator());
        List<AbstractFlexibleItem> headerVMs = new ArrayList<>(sections.size());

        for (TodoListSection section : sections) {
            TodoListHeaderViewModel headerVM = new TodoListHeaderViewModel(section.getHeader());
            headerVM.setSubItems(createSubItemList(headerVM, Lists.newArrayList(section.getItems())));
            headerVMs.add(headerVM);
        }

        if (reload) {
            mView.onTodoListReloaded(headerVMs);
        } else {
            reload = true;
            mView.onTodoListLoaded(headerVMs);
        }
    }

    private List<TodoListItemViewModel> createSubItemList(TodoListHeaderViewModel headerVM, List<TodoListItem> items) {
        Collections.sort(items, new PositionComparator());
        List<TodoListItemViewModel> itemVMs = new ArrayList<>();
        for (TodoListItem item : items) {
            itemVMs.add(new TodoListItemViewModel(headerVM, item));
        }
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
    public void syncTodoList(List<IHeader> headerItems) {
        List<IHeader> reversedHeaders = Lists.reverse(Optional
                .fromNullable(headerItems)
                .or(new ArrayList<IHeader>(0)));

        for (IHeader iHeader : reversedHeaders) {
            TodoListHeaderViewModel vm = (TodoListHeaderViewModel) iHeader;
            TodoListHeader header = vm.getHeader();
            syncHeader(header, reversedHeaders.indexOf(vm), vm.isExpanded());
            syncSubItems(vm.getSubItems());
        }
    }

    @Override
    public void deleteHeader(String uuid) {
        DeleteHeaderInteractor interactor = new DeleteHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                uuid
        );

        interactor.execute();
    }

    @Override
    public void deleteItem(String uuid) {
        DeleteItemInteractor interactor = new DeleteItemInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTodoListRepository,
                uuid
        );

        interactor.execute();
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
        List<TodoListItemViewModel> reversedItems = Lists.reverse(Optional
                .fromNullable(subItems)
                .or(new ArrayList<TodoListItemViewModel>(0)));

        for (TodoListItemViewModel vm : reversedItems) {
            TodoListItem item = vm.getItem();
            EditItemInteractor interactor = new EditItemInteractorImpl(
                    mExecutor,
                    mMainThread,
                    this,
                    mTodoListRepository,
                    item.getUuid(),
                    item.getTitle(),
                    reversedItems.indexOf(vm),
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

    @Override
    public void onItemDeleted(TodoListItem deletedItem) {
        // nothing to to
    }

    @Override
    public void onHeaderDeleted(TodoListHeader deletedHeader) {
        // nothing to to
    }
}
