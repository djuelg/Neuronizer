package de.djuelg.neuronizer.presentation.ui.flexibleadapter;

import android.support.annotation.NonNull;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;

/**
 * Created by Domi on 26.09.2017.
 */

public class SectionableAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public static final boolean STICKY = true;
    public static final int SWIPE_LEFT_TO_EDIT = 4;
    public static final int SWIPE_RIGHT_TO_DELETE = 8;

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

    public SectionableAdapter(@NonNull List<AbstractFlexibleItem> items) {
        super(items);
    }

    public TodoListHeaderViewModel evaluateOldHeader(int fromPosition, int toPosition) {
        AbstractFlexibleItem item = getItem((fromPosition < toPosition)
                ? fromPosition-1
                : fromPosition);
        if (item instanceof TodoListItemViewModel) return (TodoListHeaderViewModel) getHeaderOf(item);
        return (TodoListHeaderViewModel) item;
    }

    public int evaluateDistanceToHeader(AbstractFlexibleItem item) {
        int relPos = 0;
        for (int i = getGlobalPositionOf(item)-1; i > 0; i--) {
            if (getItem(i) instanceof TodoListHeaderViewModel) return relPos;
            relPos++;
        }
        return relPos;
    }

    public int getHeaderPosition(TodoListHeaderViewModel header) {
        List<IHeader> headerList = getHeaderItems();
        for (int i=0; i < getHeaderItems().size(); i++) {
            if (header.equals(headerList.get(i))) return i;
        }
        return -1;
    }
}
