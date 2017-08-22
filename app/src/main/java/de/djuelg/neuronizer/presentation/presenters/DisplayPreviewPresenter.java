package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.Sortation;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewViewModel;


public interface DisplayPreviewPresenter extends BasePresenter {

    void syncTodoLists(List<TodoListPreviewViewModel> previews);

    void delete(String todoListUuid);

    List<TodoListPreviewViewModel> applySortation(List<TodoListPreviewViewModel> previews, Sortation sortation);

    interface View {
        void onPreviewsLoaded(List<TodoListPreviewViewModel> previews);

    }
}
