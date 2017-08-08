package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import de.djuelg.neuronizer.presentation.presenters.AddTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.DisplayPreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayPreviewPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.dialog.Dialogs;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewViewModel;
import de.djuelg.neuronizer.storage.PreviewRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;

import static de.djuelg.neuronizer.presentation.ui.custom.AppbarTitle.changeAppbarTitle;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment implements DisplayPreviewPresenter.View, AddTodoListPresenter.View, View.OnClickListener, FlexibleAdapter.OnItemClickListener {

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreviewsLoaded(List<TodoListPreviewViewModel> previews) {
        this.previews = previews;
        mAdapter = new FlexibleAdapter<>(previews);
        mRecyclerView.setupFlexibleAdapter(this, mAdapter);
        mRecyclerView.setupRecyclerView(mEmptyView, mAdapter, mFabButton);
        mAdapter.setSwipeEnabled(true);
        mAdapter.getItemTouchHelperCallback().setSwipeThreshold(0.666F);
    }

    @Override
    public void onClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_list:
                Dialogs.showAddTodoListDialog(this);
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
        mListener.onTodoListSelected(uuid, title);
    }
}
