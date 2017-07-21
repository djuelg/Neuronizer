package de.djuelg.neuronizer.presentation.ui.custom;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

/**
 * Created by djuelg on 21.07.17.
 */

public class FlexibleRecyclerView {

    public static void setupFlexibleAdapter(Object listener, FlexibleAdapter adapter) {
        adapter.setPermanentDelete(false)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
        adapter.addListener(listener);
    }

    public static void setupRecyclerView(Context context, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter, FloatingActionButton fab) {
        FlexibleItemDecoration decoration = new FlexibleItemDecoration(context);
        decoration.withDefaultDivider();
        decoration.withDrawOver(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(fab));
    }
}
