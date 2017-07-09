package de.djuelg.neuronizer.domain.interactors;


import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.model.TodoList;


public interface DisplayAllListsInteractor extends Interactor {

    interface Callback {
        void onAllListsRetrieved(Iterable<TodoList> message);

        void onRetrievalFailed(String error);
    }
}
