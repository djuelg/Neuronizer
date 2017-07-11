package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.model.TodoList;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteTodoListInteractor {
    interface Callback {
        void onTodoListDeleted(TodoList deletedTodoList);
    }
}
