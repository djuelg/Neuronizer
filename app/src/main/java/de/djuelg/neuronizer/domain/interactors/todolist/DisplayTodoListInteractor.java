package de.djuelg.neuronizer.domain.interactors.todolist;


import java.util.List;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;


public interface DisplayTodoListInteractor extends Interactor {

    interface Callback {
        void onTodoListRetrieved(List<TodoListSection> sections);

        void onInvalidTodoListUuid();
    }
}
