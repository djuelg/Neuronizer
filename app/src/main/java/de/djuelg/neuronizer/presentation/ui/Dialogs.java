package de.djuelg.neuronizer.presentation.ui;

import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.presentation.presenters.AddHeaderPresenter;
import de.djuelg.neuronizer.presentation.presenters.AddTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.AddHeaderPresenterImpl;
import de.djuelg.neuronizer.presentation.presenters.impl.AddTodoListPresenterImpl;
import de.djuelg.neuronizer.storage.PreviewRepositoryImpl;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

/**
 * Created by djuelg on 26.07.17.
 */

public class Dialogs {

    interface DialogCallback {
        void add(String title);
    }

    public static void showAddTodoListDialog(Fragment fragment) {
        final AddTodoListPresenter presenter = new AddTodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (AddTodoListPresenter.View) fragment,
                new PreviewRepositoryImpl());

        DialogCallback callback = new DialogCallback() {
            @Override
            public void add(String title) {
                presenter.addTodoList(title);
            }
        };

        showTextInputDialog(fragment,getString(fragment, R.string.add_topic), callback);
    }

    public static void showAddHeaderDialog(Fragment fragment, final String parentTodoListUuid) {
        final AddHeaderPresenter presenter = new AddHeaderPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (AddHeaderPresenter.View) fragment,
                new TodoListRepositoryImpl()
        );

        DialogCallback callback = new DialogCallback() {
            @Override
            public void add(String title) {
                presenter.addHeader(title, parentTodoListUuid);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.add_category), callback);
    }

    private static void showTextInputDialog(Fragment fragment, String title, final DialogCallback callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragment.getContext());
        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.header_edit);

        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.add(editText.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private static String getString(Fragment fragment, @StringRes int id) {
        return fragment.getResources().getString(id);
    }
}
