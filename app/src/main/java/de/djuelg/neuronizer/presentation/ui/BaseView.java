package de.djuelg.neuronizer.presentation.ui;

import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;

/**
 * <p>
 * This interface represents a basic view. All views should implement these common methods.
 * </p>
 */
public interface BaseView {

    /**
     * This is a general method used for showing some kind of progress during a background task. For example, this
     * method should show a progress bar and/or disable buttons before some background work starts.
     */
    void showProgress();

    /**
     * This is a general method used for hiding progress information after a background task finishes.
     */
    void hideProgress();

    /**
     * This method is used for showing error messages on the UI.
     *
     * @param exceptionId The id to get correct error message
     */
    void showError(ExceptionId exceptionId);
}
