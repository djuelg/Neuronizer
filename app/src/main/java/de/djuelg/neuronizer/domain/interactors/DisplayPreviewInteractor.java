package de.djuelg.neuronizer.domain.interactors;


import de.djuelg.neuronizer.domain.interactors.base.Interactor;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;
import de.djuelg.neuronizer.domain.model.TodoListPreview;


public interface DisplayPreviewInteractor extends Interactor {

    interface Callback {
        void onPreviewsRetrieved(Iterable<TodoListPreview> lists);

        void onRetrievalFailed(ExceptionId exceptionId);
    }
}
