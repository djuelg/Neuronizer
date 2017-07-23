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
import de.djuelg.neuronizer.presentation.ui.custom.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListPreviewUI;
import eu.davidea.flexibleadapter.FlexibleAdapter;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment implements /*PreviewPresenter.View,*/ View.OnClickListener {

    @Bind(R.id.fab_add_item) FloatingActionButton mFabButton;
    @Bind(R.id.todo_list_recycler_view) FlexibleRecyclerView mRecyclerView;
    @Bind(R.id.todo_list_empty_recycler_view) RelativeLayout mEmptyView;

    //private PreviewPresenter mPresenter;
    private FragmentInteractionListener mListener;
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
        args.putString(KEY_UUID, uuid);
        args.putString(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create a presenter for this view
        Bundle bundle = getArguments();
        if (bundle != null) {
            String uuid = bundle.getString(KEY_UUID);
            String title = bundle.getString(KEY_TITLE);
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

    // TODO call to create ui items
    private void setupUIComponents(List<TodoListPreviewUI> previewUIs) {
        mAdapter = new FlexibleAdapter<>(previewUIs);
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
                mListener.onAddHeader();
                break;
        }
    }
}
