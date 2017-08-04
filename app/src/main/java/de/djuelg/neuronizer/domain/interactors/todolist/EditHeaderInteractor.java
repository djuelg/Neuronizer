package de.djuelg.neuronizer.domain.interactors.todolist;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditHeaderInteractor extends Interactor {
    interface Callback {
        void onHeaderUpdated(TodoListHeader updatedHeader);

    }
}