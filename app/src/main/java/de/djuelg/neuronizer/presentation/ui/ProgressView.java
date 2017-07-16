package de.djuelg.neuronizer.presentation.ui;

/**
 * <p>
 * This interface represents a basic view containing a progress.
 * </p>
 */
public interface ProgressView {

    /**
     * This is a general method used for showing some kind of progress during a background task. For example, this
     * method should show a progress bar and/or disable buttons before some background work starts.
     */
    void showProgress();

    /**
     * This is a general method used for hiding progress information after a background task finishes.
     */
    void hideProgress();
}
