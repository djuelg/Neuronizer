package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteTodoListInteractor extends Interactor {
    interface Callback {
        void onTodoListDeleted(TodoList deletedTodoList);
    }
}
