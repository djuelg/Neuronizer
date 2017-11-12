package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.BaseModel;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.model.preview.NotePreview;
import de.djuelg.neuronizer.domain.model.preview.Sortation;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.presentation.presenters.DisplayPreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.NotePresenter;
import de.djuelg.neuronizer.presentation.presenters.TodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayPreviewPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.custom.view.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.dialog.NoteDialogs;
import de.djuelg.neuronizer.presentation.ui.dialog.RadioDialogs;
import de.djuelg.neuronizer.presentation.ui.dialog.TodoListDialogs;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.PreviewViewModel;
import de.djuelg.neuronizer.storage.RepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.Payload;
import eu.davidea.flexibleadapter.helpers.UndoHelper;

import static de.djuelg.neuronizer.domain.model.preview.Sortation.LAST_CHANGE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_SORTING;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_TODO;
import static de.djuelg.neuronizer.presentation.ui.custom.view.Animations.fadeIn;
import static de.djuelg.neuronizer.presentation.ui.custom.view.Animations.fadeOut;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.configureAppbar;
import static de.djuelg.neuronizer.presentation.ui.dialog.NoteDialogs.showEditNoteDialog;
import static de.djuelg.neuronizer.presentation.ui.dialog.RadioDialogs.showSortingDialog;
import static de.djuelg.neuronizer.presentation.ui.dialog.TodoListDialogs.showEditTodoListDialog;
import static de.djuelg.neuronizer.presentation.ui.flexibleadapter.SectionableAdapter.SWIPE_LEFT_TO_EDIT;
import static de.djuelg.neuronizer.presentation.ui.flexibleadapter.SectionableAdapter.SWIPE_RIGHT_TO_DELETE;
import static de.djuelg.neuronizer.presentation.ui.flexibleadapter.SectionableAdapter.setupFlexibleAdapter;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment implements DisplayPreviewPresenter.View, TodoListPresenter.View, NotePresenter.View, View.OnClickListener, FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemSwipeListener, UndoHelper.OnUndoListener, RadioDialogs.SortingDialogCallback {

    @BindView(R.id.fab_add_list) FloatingActionMenu mFabMenu;
    @BindView(R.id.fab_menu_todo_list) FloatingActionButton mFabMenuTodoList;
    @BindView(R.id.fab_menu_note) FloatingActionButton mFabMenuNote;
    @BindView(R.id.preview_recycler_view) FlexibleRecyclerView mRecyclerView;
    @BindView(R.id.preview_empty_recycler_view) RelativeLayout mEmptyView;

    private DisplayPreviewPresenter mPresenter;
    private FragmentInteractionListener mFragmentListener;
    private FlexibleAdapter<PreviewViewModel> mAdapter;
    private SharedPreferences sharedPreferences;
    private Unbinder mUnbinder;

    public PreviewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment PreviewFragment.
     */
    public static PreviewFragment newInstance() {
        return new PreviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create a presenter for this view
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        mPresenter = new DisplayPreviewPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new RepositoryImpl(repositoryName)
        );

        final View view = inflater.inflate(R.layout.fragment_preview, container, false);

        mUnbinder = ButterKnife.bind(this, view);
        mFabMenu.setMenuButtonHideAnimation(fadeOut());
        mFabMenu.setMenuButtonShowAnimation(fadeIn());
        mFabMenuTodoList.setOnClickListener(this);
        mFabMenuNote.setOnClickListener(this);

        configureAppbar(getActivity(), false);
        changeAppbarTitle(getActivity(), R.string.app_name_short);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        if (mAdapter != null) {
            onDeleteConfirmed(0);
            mPresenter.syncPreviews(new ArrayList<>(mAdapter.getCurrentItems()));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mFragmentListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_preview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                showSortingDialog(this);
                break;
            case R.id.action_settings:
                mFragmentListener.onSettingsSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreviewsLoaded(List<PreviewViewModel> previews) {
        mPresenter.applySortation(previews, Sortation.parse(sharedPreferences.getInt(KEY_PREF_SORTING, LAST_CHANGE.toInt())));
        this.mAdapter = new FlexibleAdapter<>(previews);
        mRecyclerView.configure(mEmptyView, mAdapter, mFabMenu);

        boolean permanentDelete = !sharedPreferences.getBoolean(KEY_PREF_TODO, true);
        setupFlexibleAdapter(this, mAdapter, permanentDelete);
    }

    @Override
    public void sortBy(Sortation sortation) {
        if (mAdapter == null) return;
        List<PreviewViewModel> items = new ArrayList<>(mAdapter.getCurrentItems().size());
        items.addAll(mAdapter.getCurrentItems());
        items = mPresenter.applySortation(items, sortation);
        mAdapter.updateDataSet(items);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_menu_todo_list:
                TodoListDialogs.showAddTodoListDialog(this);
                break;
            case R.id.fab_menu_note:
                NoteDialogs.showAddNoteDialog(this);
                break;
            default:
                break;
        }
        mFabMenu.close(true);
    }

    @Override
    public boolean onItemClick(int position) {
        PreviewViewModel previewUI = mAdapter.getItem(position);
        if (previewUI != null && previewUI.getPreview().getBaseItem() instanceof TodoList) {
            mFragmentListener.onTodoListSelected(previewUI.getUuid(), previewUI.getTitle());
            return true;
        } else if (previewUI != null && previewUI.getPreview().getBaseItem() instanceof Note) {
            mFragmentListener.onNoteSelected(previewUI.getUuid(), previewUI.getTitle());
            return true;
        }
        return false;
    }

    @Override
    public void onTodoListAdded(String uuid, String title) {
        mPresenter.resume();
    }

    @Override
    public void onTodoListEdited(String uuid, String title) {
        mPresenter.resume();
    }

    @Override
    public void onNoteAdded(String uuid, String title) {
        mPresenter.resume();
    }

    @Override
    public void onNoteEdited(String uuid, String title) {
        mPresenter.resume();
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        switch (direction) {
            case SWIPE_LEFT_TO_EDIT:
                editItem(position);
                break;
            case SWIPE_RIGHT_TO_DELETE:
                deleteItem(position);
                break;
        }
    }

    private void editItem(int position) {
        PreviewViewModel previewVM = mAdapter.getItem(position);
        if (previewVM != null) {
            BaseModel preview = previewVM.getPreview().getBaseItem();
            if (preview instanceof TodoList) {
                showEditTodoListDialog(this, preview.getUuid(), preview.getTitle(), preview.getPosition());
            } else if (preview instanceof Note) {
                showEditNoteDialog(this, preview.getUuid(), preview.getTitle(), preview.getPosition());
            }
            mAdapter.notifyItemChanged(position);
        }
    }

    private void deleteItem(int position) {
        if (mAdapter.isPermanentDelete()) {
            permanentDeleteItem(position);
            return;
        }

        mAdapter.addSelection(position);
        String message = getString(R.string.deleted_snackbar, mAdapter.getItem(position));
        UndoHelper.OnActionListener removeListener = new UndoHelper.SimpleActionListener() {
            @Override
            public void onPostAction() {
                mAdapter.clearSelection();
                mRecyclerView.onAdapterMaybeEmpty();
            }
        };
        new UndoHelper(mAdapter, this).withPayload(Payload.CHANGE)
                .withAction(UndoHelper.ACTION_REMOVE, removeListener).remove(
                mAdapter.getSelectedPositions(), getView(), message, getString(R.string.undo), UndoHelper.UNDO_TIMEOUT);
    }

    @Override
    public void onActionStateChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // Nothing to do
    }

    @Override
    public void onUndoConfirmed(int action) {
        if (mAdapter == null || action != UndoHelper.ACTION_REMOVE) return;
        mAdapter.restoreDeletedItems();
    }

    @Override
    public void onDeleteConfirmed(int action) {
        if (mAdapter == null || mPresenter == null) return;
        for (PreviewViewModel adapterItem : mAdapter.getDeletedItems()) {
            if (adapterItem.getPreview() instanceof TodoListPreview) {
                mPresenter.deleteTodoList(adapterItem.getUuid());
            } else if (adapterItem.getPreview() instanceof NotePreview) {
                mPresenter.deleteNote(adapterItem.getUuid());
            }
        }
    }

    private void permanentDeleteItem(int position) {
        if (mAdapter == null || mPresenter == null) return;
        PreviewViewModel adapterItem = mAdapter.getItem(position);
        mAdapter.clearSelection();
        if (adapterItem != null) {
            if (adapterItem.getPreview() instanceof TodoListPreview) {
                mPresenter.deleteTodoList(adapterItem.getUuid());
            } else if (adapterItem.getPreview() instanceof NotePreview) {
                mPresenter.deleteNote(adapterItem.getUuid());
            }
            mAdapter.removeItem(position);
        }
    }
}
