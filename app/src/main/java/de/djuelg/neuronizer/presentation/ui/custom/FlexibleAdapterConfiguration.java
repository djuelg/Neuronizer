package de.djuelg.neuronizer.presentation.ui.custom;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by Domi on 22.08.2017.
 */

public class FlexibleAdapterConfiguration {

    public static void setupFlexibleAdapter(Object listener, FlexibleAdapter adapter, boolean permanentDelete) {
        adapter.setPermanentDelete(permanentDelete)
                .addListener(listener)
                .expandItemsAtStartUp()
                .setStickyHeaders(true)
                .setSwipeEnabled(true)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
        adapter.getItemTouchHelperCallback().setSwipeThreshold(0.666F);
    }
}
