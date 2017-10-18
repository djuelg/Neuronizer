package de.djuelg.neuronizer.presentation.presenters.impl;

import com.fernandocejas.arrow.collections.Lists;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.djuelg.neuronizer.domain.comparator.AlphabeticComparator;
import de.djuelg.neuronizer.domain.comparator.CreationDateComparator;
import de.djuelg.neuronizer.domain.comparator.ImportanceComparator;
import de.djuelg.neuronizer.domain.comparator.LastChangeComparator;
import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.DeleteNoteInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.DeleteTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.EditTodoListInteractor;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DeleteNoteInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DeleteTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DisplayPreviewInteractorImpl;
import de.djuelg.neuronizer.domain.interactors.preview.impl.EditTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.model.preview.Preview;
import de.djuelg.neuronizer.domain.model.preview.Sortation;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.Repository;
import de.djuelg.neuronizer.presentation.presenters.DisplayPreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.base.AbstractPresenter;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.PreviewViewModel;

/**
 * Created by dmilicic on 12/13/15.
 */
public class DisplayPreviewPresenterImpl extends AbstractPresenter implements DisplayPreviewPresenter,
        DisplayPreviewInteractor.Callback, EditTodoListInteractor.Callback, DeleteTodoListInteractor.Callback, DeleteNoteInteractor.Callback {

    private DisplayPreviewPresenter.View mView;
    private Repository repository;

    public DisplayPreviewPresenterImpl(Executor executor, MainThread mainThread,
                                       View view, Repository previewRepository) {
        super(executor, mainThread);
        mView = view;
        repository = previewRepository;
    }

    @Override
    public void resume() {
        // initialize the interactor
        DisplayPreviewInteractor interactor = new DisplayPreviewInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void pause() {
        // Nothing to do
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    @Override
    public void onPreviewsRetrieved(List<Preview> previews) {
        List<PreviewViewModel> previewVMs = new ArrayList<>();
        for (Preview preview : previews) {
                previewVMs.add(new PreviewViewModel(preview));
        }
        mView.onPreviewsLoaded(previewVMs);
    }

    @Override
    public void syncPreviews(List<PreviewViewModel> previews) {
        List<PreviewViewModel> reversedPreviews = Lists.reverse(Optional
                        .fromNullable(previews)
                        .or(new ArrayList<PreviewViewModel>(0)));

        for (PreviewViewModel vm : reversedPreviews) {
            EditTodoListInteractor interactor = new EditTodoListInteractorImpl(
                    mExecutor,
                    mMainThread,
                    this,
                    repository,
                    vm.getUuid(),
                    vm.getTitle(),
                    reversedPreviews.indexOf(vm)
            );
            interactor.execute();
        }
    }

    @Override
    public void deleteTodoList(String uuid) {
        DeleteTodoListInteractor interactor = new DeleteTodoListInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid
        );

        interactor.execute();
    }

    @Override
    public void deleteNote(String uuid) {
        DeleteNoteInteractor interactor = new DeleteNoteInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid
        );

        interactor.execute();    }

    @Override
    public List<PreviewViewModel> applySortation(List<PreviewViewModel> previews, Sortation sortation) {
        switch (sortation) {
            case IMPORTANCE:
                Collections.sort(previews, new ImportanceComparator());
                break;
            case LAST_CHANGE:
                Collections.sort(previews, new LastChangeComparator());
                break;
            case CREATION_DATE:
                Collections.sort(previews, new CreationDateComparator());
                break;
            case ALPHABETICAL:
                Collections.sort(previews, new AlphabeticComparator());
                break;
            default:
                break;
        }
        return previews;
    }

    @Override
    public void onTodoListUpdated(TodoList updatedTodoList) {
        // nothing to do
    }

    @Override
    public void onTodoListDeleted(TodoList deletedTodoList) {
        // nothing to do
    }

    @Override
    public void onNoteDeleted(Note deletedNote) {
        // nothing to do
    }
}
