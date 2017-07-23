package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewViewModel;


public interface DisplayPreviewPresenter extends BasePresenter {

    interface View {
        void displayPreviews(List<TodoListPreviewViewModel> previews);

    }
}
