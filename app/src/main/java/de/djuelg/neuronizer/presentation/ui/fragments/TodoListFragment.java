package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.presentation.presenters.AddHeaderPresenter;
import de.djuelg.neuronizer.presentation.presenters.DisplayTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayTodoListPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.Dialogs;
import de.djuelg.neuronizer.presentation.ui.custom.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment implements View.OnClickListener, DisplayTodoListPresenter.View, AddHeaderPresenter.View, View.OnLongClickListener {

    @Bind(R.id.fab_add_item) FloatingActionButton mFabButton;
    @Bind(R.id.todo_list_recycler_view) FlexibleRecyclerView mRecyclerView;
    @Bind(R.id.todo_list_empty_recycler_view) RelativeLayout mEmptyView;

    private DisplayTodoListPresenter mPresenter;
    private FragmentInteractionListener mListener;
    private FlexibleAdapter<AbstractFlexibleItem> mAdapter;
    private String uuid;
    private String title;

    public TodoListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static TodoListFragment newInstance(String uuid, String title) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UUID, uuid);
        args.putString(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create a presenter for this view
        mPresenter = new DisplayTodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TodoListRepositoryImpl()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        ButterKnife.bind(this, view);
        mFabButton.setOnClickListener(this);
        mFabButton.setOnLongClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // let's load list when the app resumes
        Bundle bundle = getArguments();
        if (bundle != null) {
            uuid = bundle.getString(KEY_UUID);
            title = bundle.getString(KEY_TITLE);
            mPresenter.loadTodoList(uuid);
            // TODO Set Actionbar Name to Todolist title
        }
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
    public void onTodoListLoaded(List<AbstractFlexibleItem> items) {
        mAdapter = new FlexibleAdapter<>(items);
        mRecyclerView.setupFlexibleAdapter(this, mAdapter);
        mRecyclerView.setupRecyclerView(mEmptyView, mAdapter, mFabButton);
        mAdapter.setSwipeEnabled(true);
        mAdapter.getItemTouchHelperCallback().setSwipeThreshold(0.666F);
    }

    @Override
    public void onRetrievalFailed() {
        // go back to previous fragment
       getActivity().onBackPressed();
    }

    @Override
    public void onClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_item:
                mListener.onAddItem(uuid);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_item:
                Dialogs.showAddHeaderDialog(this, uuid);
                return true;
        }
        return false;
    }

    @Override
    public void headerAdded() {
        // try to update dataset
        mPresenter.loadTodoList(uuid);
    }
}
