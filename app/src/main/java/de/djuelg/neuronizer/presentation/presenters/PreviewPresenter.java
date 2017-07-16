package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface PreviewPresenter extends BasePresenter {

    /**
     * Method that should signal the appropriate view to show the appropriate error with the provided message.
     */
    void onError();


    interface View {
        void displayPreviews(Iterable<TodoListPreview> previews);

        /**
         * This method is used for showing error messages on the UI.
         */
        void showNoPreviewsExisting();
    }
}
