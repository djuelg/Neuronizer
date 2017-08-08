package de.djuelg.neuronizer.presentation.ui.custom;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;

/**
 * Created by Domi on 28.03.2017.
 *
 * RecyclerViewScrollListener will hide the fab button as long as the list is scrolled
 * to the end
 */

public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final FloatingActionButton fab;

    public RecyclerViewScrollListener(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        fab.show(true);
        if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                    && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1 && firstVisibleItemPosition != 0) fab.hide(true);
        }
        super.onScrollStateChanged(recyclerView, newState);
    }
}