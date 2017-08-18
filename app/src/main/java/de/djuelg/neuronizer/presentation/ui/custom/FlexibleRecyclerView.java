package de.djuelg.neuronizer.presentation.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

/**
 * Created by djuelg on 21.07.17.
 *
 * This is custom RecyclerView with the following features:
 * - has a method to display a different view if list is empty
 * - has custom static setup methods for FlexibleAdapter and RecyclerView
 */
public class FlexibleRecyclerView extends RecyclerView {

    private View emptyView;
    private final AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && emptyView != null) {
                if(adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    FlexibleRecyclerView.this.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    FlexibleRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public FlexibleRecyclerView(Context context) {
        super(context);
    }

    public FlexibleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexibleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setupFlexibleAdapter(Object listener, FlexibleAdapter adapter, boolean permanentDelete) {
        adapter.setPermanentDelete(permanentDelete)
                .addListener(listener)
                .expandItemsAtStartUp()
                .setStickyHeaders(true)
                .setSwipeEnabled(true)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
        adapter.getItemTouchHelperCallback().setSwipeThreshold(0.666F);
    }

    public void setupRecyclerView(View emptyView, RecyclerView.Adapter adapter, View view) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        FlexibleItemDecoration decoration = new FlexibleItemDecoration(getContext());
        decoration.withDefaultDivider();
        decoration.withDrawOver(true);

        setHasFixedSize(true);
        setLayoutManager(layoutManager);
        setEmptyView(emptyView);
        setAdapter(adapter);
        addItemDecoration(decoration);
        addOnScrollListener(new RecyclerViewScrollListener(view));
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
