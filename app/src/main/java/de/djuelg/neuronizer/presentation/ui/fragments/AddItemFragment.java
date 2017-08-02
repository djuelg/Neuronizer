package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

    @Bind(R.id.header_spinner) Spinner headerSpinner;
    @Bind(R.id.editText_item_title) EditText titleEditText;
    @Bind(R.id.important_switch) SwitchCompat importantSwitch;
    @Bind(R.id.editText_item_details) EditText detailsEditText;
    @Bind(R.id.button_add_item) FloatingActionButton saveButton;
    @Bind(R.id.button_copy_title) Button copyTitleButton;
    @Bind(R.id.button_copy_details) Button copyDetailsButton;

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

        saveButton.setOnClickListener(this);
        copyTitleButton.setOnClickListener(this);
        copyDetailsButton.setOnClickListener(this);

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
                addItemWithCurrentViewInput();
                break;
            case R.id.button_copy_title:
                copyTitleToClipboard();
                break;
            case R.id.button_copy_details:
                copyDetailsToClipboard();
                break;
        }
    }

    private void copyTitleToClipboard() {
        copyToClipboard(titleEditText.getText().toString());
    }

    private void copyDetailsToClipboard() {
        copyToClipboard(detailsEditText.getText().toString());
    }

    private void copyToClipboard(String text) {
        if (text.isEmpty()){
            Toast.makeText(getActivity(), R.string.no_clipboard, Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), R.string.added_clipboard, Toast.LENGTH_SHORT).show();
    }

    private void addItemWithCurrentViewInput() {
        String title = titleEditText.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(getActivity(), R.string.title_mandatory, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean important = importantSwitch.isChecked();
        String details = detailsEditText.getText().toString();
        String headerUuid = ((TodoListHeader) headerSpinner.getSelectedItem()).getUuid();

        mPresenter.addItem(title, important, details, todoListUuid, headerUuid);
    }

    @Override
    public void itemAdded() {
        getActivity().onBackPressed();
    }

    @Override
    public void onHeadersLoaded(List<TodoListHeader> headers) {
        ArrayAdapter<TodoListHeader> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, headers);

        headerSpinner.setAdapter(spinnerAdapter);
    }
}
