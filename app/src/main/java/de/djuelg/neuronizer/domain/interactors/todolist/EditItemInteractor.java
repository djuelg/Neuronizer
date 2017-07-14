package de.djuelg.neuronizer.domain.interactors.todolist;

import de.djuelg.neuronizer.domain.model.TodoListItem;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditItemInteractor {
    interface Callback {
        void onItemUpdated(TodoListItem updatedItem);
    }
}