package de.djuelg.neuronizer.domain.interactors.todolist;

import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteItemInteractor {
    interface Callback {
        void onItemDeleted(TodoListItem deletedItem);
    }
}
