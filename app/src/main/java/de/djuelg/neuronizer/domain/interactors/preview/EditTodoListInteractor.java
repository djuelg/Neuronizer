package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.model.TodoList;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditTodoListInteractor {
    interface Callback {
        void onTodoListUpdated(TodoList updatedTodoList);
    }
}