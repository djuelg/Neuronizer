package de.djuelg.neuronizer.presentation.presenters;

import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.BaseView;


public interface MainPresenter extends BasePresenter {

    interface View extends BaseView {
        void displayPreviews(Iterable<TodoListPreview> previews);
    }
}
