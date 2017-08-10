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
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.presentation.presenters.AddHeaderPresenter;
import de.djuelg.neuronizer.presentation.presenters.DisplayTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayTodoListPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.dialog.Dialogs;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;
import static de.djuelg.neuronizer.presentation.ui.custom.Animations.fadeIn;
import static de.djuelg.neuronizer.presentation.ui.custom.Animations.fadeOut;
import static de.djuelg.neuronizer.presentation.ui.custom.AppbarTitle.changeAppbarTitle;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment implements View.OnClickListener, DisplayTodoListPresenter.View, AddHeaderPresenter.View {

    @Bind(R.id.fab_add_header) FloatingActionButton mFabHeader;
    @Bind(R.id.fab_menu) FloatingActionMenu mFabMenu;
    @Bind(R.id.fab_menu_header) FloatingActionButton mFabHeaderMenu;
    @Bind(R.id.fab_menu_item) FloatingActionButton mFabItemMenu;
    @Bind(R.id.todo_list_recycler_view) FlexibleRecyclerView mRecyclerView;
    @Bind(R.id.todo_list_empty_recycler_view) RelativeLayout mEmptyView;

    private DisplayTodoListPresenter mPresenter;
    private FragmentInteractionListener mListener;
    private FlexibleAdapter<TodoListHeaderViewModel> mAdapter;
    private String uuid;
    private String title;
    private List<TodoListHeaderViewModel> items;

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
        Bundle bundle = getArguments();
        if (bundle != null) {
            uuid = bundle.getString(KEY_UUID);
            title = bundle.getString(KEY_TITLE);
        }
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
        mFabHeader.setHideAnimation(fadeOut());
        mFabHeader.setShowAnimation(fadeIn());
        mFabMenu.setMenuButtonHideAnimation(fadeOut());
        mFabMenu.setMenuButtonShowAnimation(fadeIn());
        mFabHeader.setOnClickListener(this);
        mFabHeaderMenu.setOnClickListener(this);
        mFabItemMenu.setOnClickListener(this);
        changeAppbarTitle(getActivity(), title);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // let's load list when the app resumes
        mPresenter.loadTodoList(uuid);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.syncTodoList(items);
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
    public void onTodoListLoaded(List<TodoListHeaderViewModel> items) {
        this.items = items;
        mAdapter = new FlexibleAdapter<>(items);
        mRecyclerView.setupFlexibleAdapter(this, mAdapter);
        mRecyclerView.setupRecyclerView(mEmptyView, mAdapter, mFabMenu);
        mAdapter.setSwipeEnabled(true);
        mAdapter.getItemTouchHelperCallback().setSwipeThreshold(0.666F);
    }

    @Override
    public void onClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_header:
            case R.id.fab_menu_header:
                Dialogs.showAddHeaderDialog(this, uuid);
                break;
            case R.id.fab_menu_item:
                mListener.onAddItem(uuid);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHeaderAdded() {
        mPresenter.loadTodoList(uuid);
    }
}
