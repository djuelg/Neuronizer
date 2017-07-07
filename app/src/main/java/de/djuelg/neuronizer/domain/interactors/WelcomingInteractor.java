package de.djuelg.neuronizer.domain.interactors;


import de.djuelg.neuronizer.domain.interactors.base.Interactor;


public interface WelcomingInteractor extends Interactor {

    interface Callback {
        void onMessageRetrieved(String message);

        void onRetrievalFailed(String error);
    }
}
