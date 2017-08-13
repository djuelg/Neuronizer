package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface HeaderPresenter extends BasePresenter {

    void addHeader(String title, String parentTodoListUuid);

    void editHeader(String uuid, String title, int position, boolean expanded);

    interface View {
        void onHeaderAdded();

        void onHeaderEdited();
    }
}
