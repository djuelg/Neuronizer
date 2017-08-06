package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface AddHeaderPresenter extends BasePresenter {

    void addHeader(String title, String parentTodoListUuid);

    interface View {
        void onHeaderAdded();
    }
}
