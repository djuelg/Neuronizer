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
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.presentation.presenters.DisplayPreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.TodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayPreviewPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.custom.view.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.dialog.TodoListDialogs;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewViewModel;
import de.djuelg.neuronizer.storage.PreviewRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.Payload;
import eu.davidea.flexibleadapter.helpers.UndoHelper;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_TODO;
import static de.djuelg.neuronizer.presentation.ui.Constants.SWIPE_LEFT_TO_EDIT;
import static de.djuelg.neuronizer.presentation.ui.Constants.SWIPE_RIGHT_TO_DELETE;
import static de.djuelg.neuronizer.presentation.ui.custom.view.Animations.fadeIn;
import static de.djuelg.neuronizer.presentation.ui.custom.view.Animations.fadeOut;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;
import static de.djuelg.neuronizer.presentation.ui.custom.FlexibleAdapterConfiguration.setupFlexibleAdapter;
import static de.djuelg.neuronizer.presentation.ui.dialog.TodoListDialogs.showEditTodoListDialog;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment implements DisplayPreviewPresenter.View, TodoListPresenter.View, View.OnClickListener, FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemSwipeListener, UndoHelper.OnUndoListener {

    @Bind(R.id.fab_add_list) FloatingActionButton mFabButton;
    @Bind(R.id.preview_recycler_view) FlexibleRecyclerView mRecyclerView;
    @Bind(R.id.preview_empty_recycler_view) RelativeLayout mEmptyView;

    private DisplayPreviewPresenter mPresenter;
    private FragmentInteractionListener mListener;
    private FlexibleAdapter<TodoListPreviewViewModel> mAdapter;
    private List<TodoListPreviewViewModel> previews;

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
        // create a presenter for this view
        mPresenter = new DisplayPreviewPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new PreviewRepositoryImpl()
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_preview, container, false);

        ButterKnife.bind(this, view);
        mFabButton.setShowAnimation(fadeIn());
        mFabButton.setHideAnimation(fadeOut());
        mFabButton.setOnClickListener(this);
        changeAppbarTitle(getActivity(), R.string.app_name);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        onDeleteConfirmed(0);
        mPresenter.syncTodoLists(previews);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_preview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mListener.onSettingsSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreviewsLoaded(List<TodoListPreviewViewModel> previews) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean permanentDelete = !sharedPreferences.getBoolean(KEY_PREF_TODO, true);

        this.previews = previews;
        this.mAdapter = new FlexibleAdapter<>(previews);
        mRecyclerView.configure(mEmptyView, mAdapter, mFabButton);
        setupFlexibleAdapter(this, mAdapter, permanentDelete);
    }

    @Override
    public void onClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_list:
                TodoListDialogs.showAddTodoListDialog(this);
                break;
        }
    }

    @Override
    public boolean onItemClick(int position) {
        TodoListPreviewViewModel previewUI = mAdapter.getItem(position);
        if (previewUI != null) {
            mListener.onTodoListSelected(previewUI.getTodoListUuid(), previewUI.getTodoListTitle());
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
        TodoListPreviewViewModel previewVM = mAdapter.getItem(position);
        if (previewVM != null) {
            TodoList todoList = previewVM.getPreview().getTodoList();
            showEditTodoListDialog(this, todoList.getUuid(), todoList.getTitle(), todoList.getPosition());
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
        if (!isVisible() || action != UndoHelper.ACTION_REMOVE) return;
        mAdapter.restoreDeletedItems();
    }

    @Override
    public void onDeleteConfirmed(int action) {
        for (TodoListPreviewViewModel adapterItem : mAdapter.getDeletedItems()) {
            mPresenter.delete(adapterItem.getTodoListUuid());
        }
    }

    private void permanentDeleteItem(int position) {
        TodoListPreviewViewModel adapterItem = mAdapter.getItem(position);
        if (adapterItem == null) return;

        mAdapter.clearSelection();
        mPresenter.delete(adapterItem.getTodoListUuid());
        mAdapter.removeItem(position);
    }
}
