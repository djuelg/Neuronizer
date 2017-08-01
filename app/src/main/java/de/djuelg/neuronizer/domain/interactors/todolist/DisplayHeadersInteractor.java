package de.djuelg.neuronizer.domain.interactors.todolist;


import java.util.List;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;


public interface DisplayHeadersInteractor extends Interactor {

    interface Callback {
        void onHeaderRetrieved(List<TodoListHeader> headers);

    }
}
