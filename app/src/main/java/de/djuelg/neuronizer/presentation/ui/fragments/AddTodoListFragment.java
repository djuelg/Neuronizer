package de.djuelg.neuronizer.presentation.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.presentation.presenters.AddTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.AddTodoListPresenterImpl;
import de.djuelg.neuronizer.storage.PreviewRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

/**
 *
 */
public class AddTodoListFragment extends Fragment implements AddTodoListPresenter.View, View.OnClickListener {

    private AddTodoListPresenter mPresenter;
    private EditText editText;
    private Button addButton;
    private Button cancelButton;

    // TODO Replace Fragment with dialog
    public AddTodoListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddTodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new PreviewRepositoryImpl()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_todo_list, container, false);
        addButton = (Button) view.findViewById(R.id.button_add_todo_list);
        cancelButton = (Button) view.findViewById(R.id.button_cancel_add_todo_list);

        editText = (EditText) view.findViewById(R.id.editText_todo_list_title);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void todoListAdded() {
        getActivity().onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_todo_list:
                mPresenter.addTodoList(editText.getText().toString(), 0);
                break;
            case R.id.button_cancel_add_todo_list:
                getActivity().onBackPressed();
                break;
        }
    }
}
