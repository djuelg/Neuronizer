package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.presentation.presenters.ItemPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.ItemPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.custom.view.RichEditorNavigation;
import de.djuelg.neuronizer.storage.RepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import jp.wasabeef.richeditor.RichEditor;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_EDITOR_CONTENT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_ITEM_UUID;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;
import static de.djuelg.neuronizer.presentation.ui.custom.Clipboard.copyToClipboard;
import static de.djuelg.neuronizer.presentation.ui.custom.HtmlStripper.stripHtml;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.configureAppbar;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

/**
 *
 */
@SuppressWarnings("ConstantConditions")
public class ItemFragment extends Fragment implements ItemPresenter.View, View.OnClickListener {

    @BindView(R.id.header_spinner) Spinner headerSpinner;
    @BindView(R.id.editText_item_title) EditText titleEditText;
    @BindView(R.id.important_switch) SwitchCompat importantSwitch;
    @BindView(R.id.richEditor_item_details) RichEditor richEditor;
    @BindView(R.id.button_save_item) FloatingActionButton saveButton;
    @BindView(R.id.button_save_and_next_item) FloatingActionButton saveAndNextButton;
    @BindView(R.id.button_copy_title) ImageButton copyTitleButton;
    @BindView(R.id.button_copy_details) ImageButton copyDetailsButton;

    private ItemPresenter mPresenter;
    private FragmentInteractionListener mListener;
    private TodoListItem item;
    private String todoListUuid;
    private String itemUuid;
    private Unbinder mUnbinder;

    public ItemFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ItemFragment addItem(String todoListUuid) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UUID, todoListUuid);
        fragment.setArguments(args);
        return fragment;
    }

    public static ItemFragment editItem(String todoListUuid, String itemUuid) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UUID, todoListUuid);
        args.putString(KEY_ITEM_UUID, itemUuid);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isEditMode() {
        return itemUuid != null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        mPresenter = new ItemPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new RepositoryImpl(repositoryName)
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item, container, false);
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mUnbinder = ButterKnife.bind(this, view);

        final RichEditorNavigation richEditorNavigation = new RichEditorNavigation(view, richEditor);
        richEditorNavigation.setupRichEditor();
        richEditorNavigation.setupOnClickListeners();

        saveButton.setOnClickListener(this);
        saveAndNextButton.setOnClickListener(this);
        saveAndNextButton.setSize(FloatingActionButton.SIZE_MINI);
        copyTitleButton.setOnClickListener(this);
        copyDetailsButton.setOnClickListener(this);

        configureAppbar(getActivity(), true);

        if (savedInstanceState == null) {
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            titleEditText.requestFocus();
            loadItems();
        }

        changeAppbarTitle(getActivity(), isEditMode()
                ? R.string.fragment_edit_item
                : R.string.add_item);

        if (isEditMode()) {
            saveAndNextButton.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getString(KEY_EDITOR_CONTENT) != null) {
            richEditor.setHtml(savedInstanceState.getString(KEY_EDITOR_CONTENT));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_EDITOR_CONTENT, richEditor.getHtml());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0);
    }

    private void loadItems() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            todoListUuid = bundle.getString(KEY_UUID);
            itemUuid = bundle.getString(KEY_ITEM_UUID);
        }

        if (isEditMode()) {
            mPresenter.editMode(itemUuid);
        } else {
            mPresenter.addMode(todoListUuid);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_item:
                addOrEditItemWithCurrentViewInput(false);
                break;
            case R.id.button_save_and_next_item:
                addOrEditItemWithCurrentViewInput(true);
                break;
            case R.id.button_copy_title:
                copyTitleToClipboard();
                break;
            case R.id.button_copy_details:
                copyDetailsToClipboard();
                break;
        }
    }

    private void addOrEditItemWithCurrentViewInput(boolean addAnother) {
        String title = titleEditText.getText().toString();
        if (title.isEmpty() || mPresenter == null) {
            Toast.makeText(getActivity(), R.string.title_mandatory, Toast.LENGTH_SHORT).show();
            return;
        }

        TodoListHeader header = ((TodoListHeader) headerSpinner.getSelectedItem());
        boolean important = importantSwitch.isChecked();
        String details = (richEditor.getHtml() != null)
                ? richEditor.getHtml()
                : "";

        if(isEditMode()) {
            mPresenter.editItem(itemUuid, title, item.getPosition(), important, details, item.isDone(),
                    todoListUuid, header.getUuid());
        } else {
            mPresenter.expandHeaderOfItem(header.getUuid(), header.getTitle(), header.getPosition());
            if (addAnother) {
                mPresenter.addItemAndAnother(title, important, details, todoListUuid, header.getUuid());
            } else {
                mPresenter.addItem(title, important, details, todoListUuid, header.getUuid());
            }
        }
    }

    private void copyTitleToClipboard() {
        copyToClipboard(getContext(), titleEditText.getText().toString());
    }

    private void copyDetailsToClipboard() {
        String html = richEditor.getHtml();
        copyToClipboard(getContext(), stripHtml((html != null) ? html : ""));
    }

    @Override
    public void itemSynced(boolean addAnother) {
        if (addAnother) {
            mListener.onAddAnotherItem(todoListUuid);
            return;
        }
        getActivity().onBackPressed();
    }

    @Override
    public void onHeadersLoaded(List<TodoListHeader> headers) {
        ArrayAdapter<TodoListHeader> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, headers);
        headerSpinner.setAdapter(spinnerAdapter);

        for (TodoListHeader header : headers) {
            if (isEditMode() && header.getUuid().equals(item.getParentHeaderUuid()))
                headerSpinner.setSelection(headers.indexOf(header));
        }
    }

    @Override
    public void onItemLoaded(TodoListItem item) {
        this.item = item;

        titleEditText.append(item.getTitle());
        importantSwitch.setChecked(item.isImportant());
        richEditor.setHtml(item.getDetails());

        // load headers after item retrieved in editMode mode
        mPresenter.addMode(todoListUuid);
    }
}
