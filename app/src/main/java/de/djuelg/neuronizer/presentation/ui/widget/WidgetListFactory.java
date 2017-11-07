package de.djuelg.neuronizer.presentation.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fernandocejas.arrow.collections.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.comparator.PositionComparator;
import de.djuelg.neuronizer.domain.model.BaseModel;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TODO_LIST;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<BaseModel> itemList;
    private final Context context;
    private final String uuid;
    private final TodoListRepository repository;

    WidgetListFactory(Context context, Intent intent) {
        this.itemList = new ArrayList<>();
        this.context = context;
        this.uuid = intent.getStringExtra(KEY_UUID);
        this.repository = new TodoListRepositoryImpl(intent.getStringExtra(KEY_TODO_LIST));
    }

    @Override
    public void onCreate() {
        // nothing to do
    }

    @Override
    public void onDataSetChanged() {
        itemList.clear();
        List<TodoListSection> sections = Lists.newArrayList(repository.getSectionsOfTodoListId(uuid));
        Collections.sort(sections, new PositionComparator());

        for (TodoListSection section : sections) {
            itemList.add(section.getHeader());
            List<TodoListItem> items = Lists.newArrayList(section.getItems());
            Collections.sort(items, new PositionComparator());
            itemList.addAll(items);
        }
    }

    @Override
    public void onDestroy() {
        // nothing to do
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.widget_todo_list_item);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= itemList.size()) return null;

        BaseModel listItem = itemList.get(position);
        if (listItem instanceof TodoListHeader) {
            return remoteViewsFor((TodoListHeader) listItem);
        }
        return remoteViewsFor((TodoListItem) listItem);
    }

    private RemoteViews remoteViewsFor(TodoListHeader item) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_todo_list_header);
        remoteViews.setTextViewText(R.id.widget_todo_list_header_title, item.getTitle());
        return remoteViews;
    }

    private RemoteViews remoteViewsFor(TodoListItem item) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.widget_todo_list_item);

        applyDefaultViewOn(remoteViews);
        SpannableString title = new SpannableString(item.getTitle());
        if (item.isImportant()) applyImportanceViewOn(remoteViews, title);
        if (item.isDone()) applyDoneViewOn(remoteViews, title);
        if (item.hasDetails()) applyDetailViewOn(remoteViews);
        remoteViews.setTextViewText(R.id.widget_todo_list_item_title, title);

        return remoteViews;
    }

    private void applyDefaultViewOn(RemoteViews remoteViews) {
        remoteViews.setTextColor(R.id.widget_todo_list_item_title, context.getResources().getColor(R.color.dark_gray));
        remoteViews.setImageViewResource(R.id.widget_todo_list_item_details, R.drawable.ic_lightbulb_outline_gray_24dp);
        remoteViews.setViewVisibility(R.id.widget_todo_list_item_details, GONE);
    }

    private void applyImportanceViewOn(RemoteViews remoteViews, SpannableString title) {
        title.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
        remoteViews.setTextColor(R.id.widget_todo_list_item_title, context.getResources().getColor(R.color.colorPrimary));
        remoteViews.setImageViewResource(R.id.widget_todo_list_item_details, R.drawable.ic_lightbulb_outline_primary_24dp);
    }

    private void applyDoneViewOn(RemoteViews remoteViews, SpannableString title) {
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        title.setSpan(strikethroughSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        remoteViews.setTextColor(R.id.widget_todo_list_item_title, context.getResources().getColor(R.color.light_gray));
        remoteViews.setImageViewResource(R.id.widget_todo_list_item_details, R.drawable.ic_lightbulb_outline_light_gray_24dp);
    }

    private void applyDetailViewOn(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.widget_todo_list_item_details, VISIBLE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WidgetListFactory)) return false;
        WidgetListFactory that = (WidgetListFactory) o;
        return Objects.equals(itemList, that.itemList) &&
                Objects.equals(context, that.context) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(repository, that.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemList, context, uuid, repository);
    }
}