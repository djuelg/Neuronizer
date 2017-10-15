package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.Sortation;
import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.PreviewViewModel;

public interface DisplayPreviewPresenter extends BasePresenter {

    void syncPreviews(List<PreviewViewModel> previews);

    void deleteTodoList(String uuid);

    void deleteNote(String uuid);

    List<PreviewViewModel> applySortation(List<PreviewViewModel> previews, Sortation sortation);

    interface View {
        void onPreviewsLoaded(List<PreviewViewModel> previews);

    }
}
