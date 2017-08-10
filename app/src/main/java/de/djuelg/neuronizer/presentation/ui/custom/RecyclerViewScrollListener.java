package de.djuelg.neuronizer.presentation.ui.custom;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by Domi on 28.03.2017.
 *
 * RecyclerViewScrollListener will show the view button as long as the list is scrolled
 * to the end
 */

public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final View view;

    public RecyclerViewScrollListener(View view) {
        this.view = view;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        show(view);
        if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                    && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1 && firstVisibleItemPosition != 0) hide(view);
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    private void hide(View view) {
        if (view instanceof FloatingActionButton) {
            ((FloatingActionButton)view).hide(true);
        } else if (view instanceof FloatingActionMenu) {
            ((FloatingActionMenu)view).hideMenu(true);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void show(View view) {
        if (view instanceof FloatingActionButton) {
            ((FloatingActionButton)view).show(true);
        } else if (view instanceof FloatingActionMenu) {
            ((FloatingActionMenu)view).showMenu(true);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}