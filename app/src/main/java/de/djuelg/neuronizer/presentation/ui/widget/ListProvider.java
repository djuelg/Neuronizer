package de.djuelg.neuronizer.presentation.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.support.v7.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fernandocejas.arrow.collections.Iterables;
import com.fernandocejas.arrow.collections.Lists;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.TodoListUsable;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.presentation.ui.Constants;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TODO_LIST;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

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
        final RemoteViews remoteView;
        TodoListUsable listItem = itemList.get(position);

        if (listItem instanceof TodoListItem) {
            remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.widget_todo_list_item);

            String title = listItem.getTitle();
            TodoListItem todoListItem = (TodoListItem) listItem;

            if (!todoListItem.getDetails().isEmpty()) {
                title = Constants.WIDGET_DETAIL_EMOJI + " " + title;
            }
            remoteView.setTextViewText(R.id.widget_todo_list_item_title, title);

            if (todoListItem.isImportant()) {
                SpannableString fatTitle = new SpannableString(title);
                fatTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, fatTitle.length(), 0);
                remoteView.setTextColor(R.id.widget_todo_list_item_title,
                        context.getResources().getColor(R.color.colorPrimary));
                remoteView.setTextViewText(R.id.widget_todo_list_item_title, fatTitle);
            } else {
                SpannableString normalTitle = new SpannableString(title);
                normalTitle.setSpan(new StyleSpan(Typeface.NORMAL), 0, normalTitle.length(), 0);
                remoteView.setTextColor(R.id.widget_todo_list_item_title,
                        context.getResources().getColor(R.color.dark_gray));
                remoteView.setTextViewText(R.id.widget_todo_list_item_title, normalTitle);
            }
        } else { // Header
            remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.widget_todo_list_header);
            remoteView.setInt(R.id.widget_todo_list_header_container, "setBackgroundColor",
                    context.getResources().getColor(R.color.colorPrimary));
            remoteView.setTextViewText(R.id.widget_todo_list_header_title, listItem.getTitle());
        }

        return remoteView;
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