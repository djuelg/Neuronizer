package de.djuelg.neuronizer.presentation.ui.dialog;

import android.support.v4.app.Fragment;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.presentation.presenters.TodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.TodoListPresenterImpl;
import de.djuelg.neuronizer.storage.PreviewRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

import static de.djuelg.neuronizer.presentation.ui.dialog.BaseDialogs.getString;
import static de.djuelg.neuronizer.presentation.ui.dialog.BaseDialogs.showTextInputDialog;

/**
 * Created by djuelg on 26.07.17.
 */

public class TodoListDialogs {

    public static void showAddTodoListDialog(Fragment fragment) {
        final TodoListPresenter presenter = new TodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (TodoListPresenter.View) fragment,
                new PreviewRepositoryImpl());

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.addTodoList(title);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.add_topic), callback);
    }
}
