package de.djuelg.neuronizer.domain.interactors.todolist;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditItemInteractor extends Interactor {
    interface Callback {
        void onItemUpdated(TodoListItem updatedItem);

        void onItemNotFound();
    }
}