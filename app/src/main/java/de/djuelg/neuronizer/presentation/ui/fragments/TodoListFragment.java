package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.flexibleAdapter.TodoListPreviewUI;
import eu.davidea.flexibleadapter.FlexibleAdapter;

import static de.djuelg.neuronizer.presentation.ui.custom.FlexibleRecyclerView.setupFlexibleAdapter;
import static de.djuelg.neuronizer.presentation.ui.custom.FlexibleRecyclerView.setupRecyclerView;

/**
 * Activities that contain this fragment must implement the
 * {@link OnInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment implements /*PreviewPresenter.View,*/ View.OnClickListener {

    @Bind(R.id.fab_add_item) FloatingActionButton mFabButton;
    @Bind(R.id.todo_list_recycler_view) RecyclerView mRecyclerView;

    //private PreviewPresenter mPresenter;
    private OnInteractionListener mListener;
    private FlexibleAdapter<TodoListPreviewUI> mAdapter;

    public TodoListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uuid Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment PreviewFragment.
     */
    public static TodoListFragment newInstance(String uuid, String title) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putString("UUID", uuid);
        args.putString("TITLE", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create a presenter for this view
        Bundle bundle = getArguments();
        if (bundle != null) {
            // TODO Move to constant everywhere
            String uuid = bundle.getString("UUID");
            String title = bundle.getString("TITLE");
            /*
        mPresenter = new DisplayTodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TodoListRepositoryImpl(),
                uuid
        );
        */
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        ButterKnife.bind(this, view);
        mFabButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // let's start welcome message retrieval when the app resumes
        //mPresenter.resume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInteractionListener) {
            mListener = (OnInteractionListener) context;
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

    // TODO call to create ui items
    private void setupUIComponents(List<TodoListPreviewUI> previewUIs) {
        mAdapter = new FlexibleAdapter<>(previewUIs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((mRecyclerView.getContext()));
        setupFlexibleAdapter(this, mAdapter);
        setupRecyclerView(getContext(), mRecyclerView, layoutManager, mAdapter, mFabButton);
        mAdapter.setSwipeEnabled(true);
        mAdapter.getItemTouchHelperCallback().setSwipeThreshold(0.666F);
    }

    @Override
    public void onClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_list:
                mListener.onAddHeader();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    // TODO Einen gemeinsamen Listener für alle Fragments erstellen
    public interface OnInteractionListener {
        void onAddHeader();

        void onAddItem();

        void onMarkdownHelpSelected();
    }
}
