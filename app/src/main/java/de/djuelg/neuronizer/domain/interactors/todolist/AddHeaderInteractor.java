package de.djuelg.neuronizer.domain.interactors.todolist;

import de.djuelg.neuronizer.domain.interactors.base.Interactor;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddHeaderInteractor extends Interactor  {
    interface Callback {
        void onHeaderAdded();

        void onParentNotFound();
    }
}
