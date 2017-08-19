package de.djuelg.neuronizer.presentation.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import de.djuelg.neuronizer.R;
import jp.wasabeef.richeditor.RichEditor;

import static de.djuelg.neuronizer.presentation.ui.custom.view.RichEditorNavigation.setupRichEditor;

/**
 * Created by djuelg on 26.07.17.
 */

public class BaseDialogs {

    interface InputDialogCallback {
        void update(String title);
    }

    static void showTextInputDialog(final Fragment fragment, String title, final InputDialogCallback callback) {
        showTextInputDialog(fragment, title, callback, null);
    }


    static void showTextInputDialog(final Fragment fragment, String dialogTitle, final InputDialogCallback callback, @Nullable String itemTitle) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragment.getContext());
        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.input_dialog, null);
        dialogBuilder.setView(dialogView);

        final InputMethodManager inputManager = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final EditText editText = dialogView.findViewById(R.id.header_edit);
        if (itemTitle != null) editText.append(itemTitle);
        editText.requestFocus();

        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(fragment.getActivity(), R.string.title_mandatory, Toast.LENGTH_SHORT).show();
                } else {
                    callback.update(editText.getText().toString());
                }
                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                dialog.dismiss();
            }
        });
        dialogBuilder.create().show();
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showTextDialog(Context context, String title, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.done, null);
        dialogBuilder.create().show();
    }

    public static void showHtmlDialog(Context context, String title, String htmlMessage) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = inflater.inflate(R.layout.rich_text_dialog, null);
        dialogBuilder.setView(dialogView);
        RichEditor richEditor = dialogView.findViewById(R.id.richEditor_dialog);
        richEditor.setHtml(htmlMessage);
        richEditor.setInputEnabled(false);
        richEditor.setFocusable(false);
        setupRichEditor(richEditor);

        dialogBuilder.setTitle(context.getResources().getString(R.string.details_title, title));
        dialogBuilder.setPositiveButton(R.string.done, null);
        dialogBuilder.create().show();
    }

    static String getString(Context context, @StringRes int id) {
        return context.getResources().getString(id);
    }

    static String getString(Fragment fragment, @StringRes int id) {
        return fragment.getResources().getString(id);
    }
}
