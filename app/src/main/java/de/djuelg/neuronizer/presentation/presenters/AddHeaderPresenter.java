package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.domain.model.todolist.Color;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;


public interface AddHeaderPresenter extends BasePresenter {

    void addHeader(String title, Color color, String parentTodoListUuid);

    interface View {
        void headerAdded();
    }
}
