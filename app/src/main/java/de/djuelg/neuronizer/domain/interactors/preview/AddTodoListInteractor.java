package de.djuelg.neuronizer.domain.interactors.preview;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddTodoListInteractor extends Interactor {
    interface Callback {
        void onTodoListAdded(String uuid, String title);
    }
}
