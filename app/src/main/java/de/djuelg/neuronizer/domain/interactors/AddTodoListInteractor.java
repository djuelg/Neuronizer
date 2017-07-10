package de.djuelg.neuronizer.domain.interactors;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddTodoListInteractor {
    interface Callback {
        void onTodoListAdded();
    }
}
