package de.djuelg.neuronizer.presentation.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.presentation.presenters.AddItemPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.AddItemPresenterImpl;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TODO_LIST_UUID;

/**
 *
 */
public class AddItemFragment extends Fragment implements AddItemPresenter.View, View.OnClickListener {

    @Bind(R.id.editText_item_title) EditText editText;
    @Bind(R.id.button_add_item) Button addButton;
    @Bind(R.id.button_cancel_add_item) Button cancelButton;

    private AddItemPresenter mPresenter;
    private String todoListUuid;

    public AddItemFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static AddItemFragment newInstance(String todoListUuid) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TODO_LIST_UUID, todoListUuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddItemPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TodoListRepositoryImpl()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        ButterKnife.bind(this, view);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // load headers and save todoListUuid
        Bundle bundle = getArguments();
        if (bundle != null) {
            todoListUuid = bundle.getString(KEY_TODO_LIST_UUID);
            mPresenter.getHeaders(todoListUuid);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_item:
                mPresenter.addItem(editText.getText().toString(), false, "", todoListUuid, "headerUUID-FromView");
                break;
            case R.id.button_cancel_add_item:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void itemAdded() {
        getActivity().onBackPressed();
    }

    @Override
    public void onHeadersLoaded(List<TodoListHeader> headers) {
        // TODO load headers into displaying view
    }
}
