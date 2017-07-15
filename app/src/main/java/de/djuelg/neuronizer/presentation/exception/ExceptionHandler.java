package de.djuelg.neuronizer.presentation.exception;

import android.content.res.Resources;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;

/**
 * Created by djuelg on 09.07.17.
 *
 * Convenience class to display exception message in correct language
 */

public class ExceptionHandler {
    public static String getMessage(Resources resources, ExceptionId exceptionId) {
        final String message;
        switch (exceptionId) {
            case NO_LISTS:
                message = resources.getString(R.string.exception_no_list);
                break;
            default:
                message = resources.getString(R.string.exception_unknown);
                break;
        }
        return message;
    }
}
