package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.fernandocejas.arrow.optional.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.presentation.presenters.DisplayNotePresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayNotePresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.custom.ShareIntent;
import de.djuelg.neuronizer.presentation.ui.custom.view.RichEditorNavigation;
import de.djuelg.neuronizer.storage.RepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import jp.wasabeef.richeditor.RichEditor;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_EDITOR_CONTENT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
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
public class NoteFragment extends Fragment implements DisplayNotePresenter.View, View.OnClickListener {

    @BindView(R.id.richEditor_item_details) RichEditor richEditor;
    @BindView(R.id.button_save_item) FloatingActionButton saveButton;

    private FragmentInteractionListener mListener;
    private DisplayNotePresenter mPresenter;
    private Unbinder mUnbinder;
    private String uuid;
    private String title;

    public NoteFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static NoteFragment newInstance(String uuid, String title) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UUID, uuid);
        args.putString(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
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
        mPresenter = new DisplayNotePresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new RepositoryImpl(repositoryName)
        );

        Bundle bundle = getArguments();
        if (bundle != null) {
            uuid = bundle.getString(KEY_UUID);
            title = bundle.getString(KEY_TITLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_note, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        saveButton.setOnClickListener(this);

        final RichEditorNavigation richEditorNavigation = new RichEditorNavigation(view, richEditor);
        richEditorNavigation.setupRichEditor();
        richEditorNavigation.setupOnClickListeners();

        configureAppbar(getActivity(), true);
        changeAppbarTitle(getActivity(), title);

        if (savedInstanceState == null) {
            mPresenter.loadNote(uuid);
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        saveNoteToRepository();
    }


    @Override
    public void onNoteLoaded(Optional<Note> note) {
        if (!note.isPresent()) {
            // The note with uuid doesn't exist -> return to previous fragment
            getFragmentManager().popBackStack();
            return;
        }

        richEditor.setHtml(note.get().getBody());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
            case R.id.action_clipboard:
                copyHtmlAsTextToClipboard();
                return true;
            case R.id.action_settings:
                mListener.onSettingsSelected();
                return true;
            case R.id.action_share:
                ShareIntent.withTitle(title).withHtml(richEditor.getHtml()).send(getContext());
                return true;
        }
        return false;
    }

    private void saveNoteToRepository() {
        mPresenter.editNote(uuid, richEditor.getHtml());
    }

    private void copyHtmlAsTextToClipboard() {
        String html = richEditor.getHtml();
        copyToClipboard(getContext(), stripHtml((html != null) ? html : ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_item:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
