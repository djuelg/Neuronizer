package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewUI;


public interface PreviewPresenter extends BasePresenter {

    interface View {
        void displayPreviews(List<TodoListPreviewUI> previews);

    }
}
