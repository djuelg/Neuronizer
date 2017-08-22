package de.djuelg.neuronizer.presentation.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.preview.Sortation;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_SORTING;
import static de.djuelg.neuronizer.presentation.ui.dialog.BaseDialogs.getString;

/**
 * Created by Domi on 22.08.2017.
 */

public class RadioDialogs {

    interface RadioDialogCallback {
        void which(int position);
    }

    public interface SortingDialogCallback {
        void sortBy(Sortation sortation);
    }

    public static void showSortingDialog(final Fragment fragment) {

        RadioDialogCallback callback = new RadioDialogCallback() {
            @Override
            public void which(int position) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_PREF_SORTING, position);
                editor.apply();

                SortingDialogCallback fragmentCallback = (SortingDialogCallback) fragment;
                fragmentCallback.sortBy(Sortation.parse(position));
            }
        };

        CharSequence[] items = new CharSequence[]{
                getString(fragment, R.string.sort_most_used),
                getString(fragment, R.string.sort_last_change),
                getString(fragment, R.string.sort_creation),
                getString(fragment, R.string.sort_alphabetically)
        };
        showRadioDialog(fragment.getContext(), getString(fragment, R.string.sort_dialog), items, callback);
    }

    private static void showRadioDialog(Context context, String title, CharSequence items[], final RadioDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.which(which);
            }
        });
        builder.show();
    }
}
