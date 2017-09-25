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
import java.util.List;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.TodoListUsable;
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
class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private final List<TodoListUsable> itemList;
    private final Context context;
    private final String repositoryName;
    private final String uuid;

    ListProvider(Context context, Intent intent) {
        this.itemList = new ArrayList<>();
        this.context = context;
        this.repositoryName = intent.getStringExtra(KEY_TODO_LIST);
        this.uuid = intent.getStringExtra(KEY_UUID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        itemList.clear();
        TodoListRepository repository = new TodoListRepositoryImpl(repositoryName);
        List<TodoListSection> sections = Lists.newArrayList(repository.getSectionsOfTodoListId(uuid));

        for (TodoListSection section : Lists.reverse(sections)) {
            itemList.add(section.getHeader());
            itemList.addAll(Lists.reverse(Lists.newArrayList(section.getItems())));
        }

    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews;
        TodoListUsable listItem = itemList.get(position);

        if (listItem instanceof TodoListItem) {
            remoteViews = setupItem((TodoListItem) listItem);
        } else {
            remoteViews = setupHeader((TodoListHeader) listItem);
        }

        return remoteViews;
    }

    public RemoteViews setupItem(TodoListItem item) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.widget_todo_list_item);

        displayIfItemIsImportant(remoteViews, item);
        displayIfItemHasDetails(remoteViews, item);
        displayIfItemIsDone(remoteViews, item);

        return remoteViews;
    }

    private void displayIfItemIsDone(RemoteViews remoteViews, TodoListItem item) {
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        SpannableString title = new SpannableString(item.getTitle());

        if (item.isDone()) {
            remoteViews.setTextColor(R.id.widget_todo_list_item_title, context.getResources().getColor(R.color.light_gray));
            title.setSpan(strikethroughSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            remoteViews.setImageViewResource(R.id.widget_todo_list_item_details, R.drawable.ic_lightbulb_outline_light_gray_24dp);
        }
        remoteViews.setTextViewText(R.id.widget_todo_list_item_title, title);
    }

    private void displayIfItemHasDetails(RemoteViews remoteViews, TodoListItem item) {
        if (item.getDetails().isEmpty()) {
            remoteViews.setViewVisibility(R.id.widget_todo_list_item_details, GONE);
        } else {
            remoteViews.setViewVisibility(R.id.widget_todo_list_item_details, VISIBLE);
        }
    }

    private void displayIfItemIsImportant(RemoteViews remoteViews, TodoListItem item) {
        SpannableString title = new SpannableString(item.getTitle());
        if (item.isImportant()) {
            title.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
            remoteViews.setTextColor(R.id.widget_todo_list_item_title, context.getResources().getColor(R.color.colorPrimary));
            remoteViews.setImageViewResource(R.id.widget_todo_list_item_details, R.drawable.ic_lightbulb_outline_primary_24dp);
        } else {
            title.setSpan(new StyleSpan(Typeface.NORMAL), 0, title.length(), 0);
            remoteViews.setTextColor(R.id.widget_todo_list_item_title, context.getResources().getColor(R.color.dark_gray));
            remoteViews.setImageViewResource(R.id.widget_todo_list_item_details, R.drawable.ic_lightbulb_outline_gray_24dp);
        }
        remoteViews.setTextViewText(R.id.widget_todo_list_item_title, title);
    }

    private RemoteViews setupHeader(TodoListHeader item) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_todo_list_header);
        remoteViews.setTextViewText(R.id.widget_todo_list_header_title, item.getTitle());
        return remoteViews;
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
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onDestroy() {
    }
}