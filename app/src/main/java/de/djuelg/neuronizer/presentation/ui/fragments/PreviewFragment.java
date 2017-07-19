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
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.presentation.presenters.PreviewPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.PreviewPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.RecyclerViewScrollListener;
import de.djuelg.neuronizer.storage.PreviewRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Activities that contain this fragment must implement the
 * {@link OnInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment implements PreviewPresenter.View, View.OnClickListener {

    @Bind(R.id.welcome_textview) TextView mWelcomeTextView;
    @Bind(R.id.fab_add_list) FloatingActionButton mFabButton;
    @Bind(R.id.preview_recycler_view) RecyclerView mRecyclerView;

    private PreviewPresenter mPresenter;
    private OnInteractionListener mListener;
    private FlexibleAdapter<IFlexible> mAdapter;

    public PreviewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreviewFragment.
     */
    // TODO: Rename and change types and number of parameters or remove
    public static PreviewFragment newInstance(String param1, String param2) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putString("PARAM1", param1);
        args.putString("PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create a presenter for this view
        mPresenter = new PreviewPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new PreviewRepositoryImpl()
        );

        // TODO Pass list from database
        mAdapter = new FlexibleAdapter<>(null);
        setupFlexibleAdapter(mAdapter);
    }

    private void setupFlexibleAdapter(FlexibleAdapter adapter) {
        adapter.setPermanentDelete(false)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_preview, container, false);

        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((mRecyclerView.getContext()));
        setupRecyclerView(mRecyclerView, layoutManager, mAdapter, mFabButton);

        mFabButton.setOnClickListener(this);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter, FloatingActionButton fab) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(fab));
    }

    @Override
    public void onResume() {
        super.onResume();

        // let's start welcome message retrieval when the app resumes
        mPresenter.resume();
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

    @Override
    public void showNoPreviewsExisting() {
        mWelcomeTextView.setText(getResources().getString(R.string.no_preview_existing));
    }

    @Override
    public void displayPreviews(Iterable<TodoListPreview> previews) {
        mWelcomeTextView.setText(previews.iterator().next().toString());
    }

    @Override
    public void onClick(View view) {
        mListener.onAddTodoList();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnInteractionListener {
        void onTodoListSelected(String uuid, String title);

        void onAddTodoList();
    }
}
