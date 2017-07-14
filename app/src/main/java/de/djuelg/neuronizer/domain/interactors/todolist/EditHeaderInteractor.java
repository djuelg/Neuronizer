package de.djuelg.neuronizer.domain.interactors.todolist;

import de.djuelg.neuronizer.domain.model.TodoListHeader;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditHeaderInteractor {
    interface Callback {
        void onHeaderUpdated(TodoListHeader updatedHeader);
    }
}