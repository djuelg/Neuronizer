package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface AddTodoListPresenter extends BasePresenter {

    /**
     * Method that should signal the appropriate view to show the appropriate error with the provided message.
     */
    void onError();

    void addTodoList(String title, int position);

    interface View {
        void todoListAdded();
    }
}
