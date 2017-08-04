package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;


public interface DisplayTodoListPresenter extends BasePresenter {

    void loadTodoList(String uuid);

    void syncTodoList(List<TodoListHeaderViewModel> headerItems);

    interface View {
        void onTodoListLoaded(List<TodoListHeaderViewModel> items);

    }
}
