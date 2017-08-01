package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface AddItemPresenter extends BasePresenter {

    void addItem(String title, boolean important, String Details, String parentTodoListUuid, String parentHeaderUuid);

    void getHeaders(String todoListUuid);

    interface View {
        void itemAdded();

        void onHeadersLoaded(List<TodoListHeader> headers);
    }
}
