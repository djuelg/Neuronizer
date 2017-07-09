package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.BaseView;


public interface MainPresenter extends BasePresenter {

    interface View extends BaseView {
        void displayAllLists(Iterable<TodoList> lists);
    }
}
