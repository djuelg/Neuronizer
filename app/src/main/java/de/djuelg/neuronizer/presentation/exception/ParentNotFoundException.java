package de.djuelg.neuronizer.presentation.exception;

/**
 * Created by Domi on 03.08.2017.
 */

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException(String message) {
        super(message);
    }
}
