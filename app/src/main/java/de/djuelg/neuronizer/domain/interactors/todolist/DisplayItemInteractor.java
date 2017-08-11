package de.djuelg.neuronizer.domain.interactors.todolist;


import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;


public interface DisplayItemInteractor extends Interactor {

    interface Callback {
        void onItemRetrieved(TodoListItem item);

    }
}
