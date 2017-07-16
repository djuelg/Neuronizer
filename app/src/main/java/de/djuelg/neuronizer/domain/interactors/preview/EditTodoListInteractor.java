package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.TodoList;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditTodoListInteractor extends Interactor {
    interface Callback {
        void onTodoListUpdated(TodoList updatedTodoList);
    }
}