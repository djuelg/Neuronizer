package de.djuelg.neuronizer.presentation.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;
import com.rustamg.filedialogs.SaveFileDialog;

import de.djuelg.neuronizer.R;

import static de.djuelg.neuronizer.storage.RepositoryManager.REPOSITORY_EXTENSION;

/**
 * Created by Domi on 07.09.2017.
 */

public class FileDialogs {

    public static void showOpenFileDialog(FragmentActivity activity) {
        showFileDialog(activity, new OpenFileDialog());
    }

    public static void showSaveFileDialog(FragmentActivity activity) {
        showFileDialog(activity, new SaveFileDialog());
    }

    private static void showFileDialog(FragmentActivity activity, FileDialog dialog) {
        Bundle args = new Bundle();
        args.putString(FileDialog.EXTENSION, REPOSITORY_EXTENSION);
        dialog.setArguments(args);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        dialog.show(activity.getSupportFragmentManager(), OpenFileDialog.class.getName());
    }
}
