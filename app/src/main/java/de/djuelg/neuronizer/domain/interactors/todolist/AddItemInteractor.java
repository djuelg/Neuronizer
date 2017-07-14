package de.djuelg.neuronizer.domain.interactors.todolist;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddItemInteractor {
    interface Callback {
        void onItemAdded();
    }
}
