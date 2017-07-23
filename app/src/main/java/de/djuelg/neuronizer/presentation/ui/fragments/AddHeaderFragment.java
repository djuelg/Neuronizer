package de.djuelg.neuronizer.presentation.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.todolist.Color;
import de.djuelg.neuronizer.presentation.presenters.AddHeaderPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.AddHeaderPresenterImpl;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;

/**
 *
 */
public class AddHeaderFragment extends Fragment implements AddHeaderPresenter.View, View.OnClickListener {

    @Bind(R.id.editText_todo_list_title) EditText editText;
    @Bind(R.id.button_add_todo_list) Button addButton;
    @Bind(R.id.button_cancel_add_todo_list) Button cancelButton;

    private AddHeaderPresenter mPresenter;

    public AddHeaderFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static AddHeaderFragment newInstance(String uuid) {
        AddHeaderFragment fragment = new AddHeaderFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UUID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddHeaderPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TodoListRepositoryImpl()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO implement separated layout
        final View view = inflater.inflate(R.layout.fragment_add_header, container, false);
        ButterKnife.bind(this, view);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void headerAdded() {
        getActivity().onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_todo_list:
                // TODO add header from ui fields
                //addHeader(...);
                getActivity().onBackPressed();
                break;
            case R.id.button_cancel_add_todo_list:
                getActivity().onBackPressed();
                break;
        }
    }

    private void addHeader(String title, Color color) {
        Bundle bundle = getArguments();
        mPresenter.resume();
        if (bundle != null) {
            String parentTodoListUuid = bundle.getString(KEY_UUID);

            mPresenter.addHeader(title, color, parentTodoListUuid);
        }
    }
}
