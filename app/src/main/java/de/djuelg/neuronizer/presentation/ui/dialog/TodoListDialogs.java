package de.djuelg.neuronizer.presentation.ui.dialog;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.presentation.presenters.TodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.TodoListPresenterImpl;
import de.djuelg.neuronizer.storage.RepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.dialog.BaseDialogs.getString;
import static de.djuelg.neuronizer.presentation.ui.dialog.BaseDialogs.showTextInputDialog;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

/**
 * Created by djuelg on 26.07.17.
 */

public class TodoListDialogs {

    public static void showAddTodoListDialog(Fragment fragment) {
        final TodoListPresenter presenter = instantiatePresenterUsing(fragment);

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.addTodoList(title);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.add_todo_list), callback);
    }

    public static void showEditTodoListDialog(Fragment fragment, final String uuid, final String oldTitle, final int position) {
        final TodoListPresenter presenter = instantiatePresenterUsing(fragment);

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.editTodoList(uuid, title, position);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.edit_topic), callback, oldTitle);
    }

    private static TodoListPresenter instantiatePresenterUsing(Fragment fragment) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        return new TodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (TodoListPresenter.View) fragment,
                new RepositoryImpl(repositoryName));
    }
}
