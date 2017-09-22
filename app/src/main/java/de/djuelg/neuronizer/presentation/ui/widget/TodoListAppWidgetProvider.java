package de.djuelg.neuronizer.presentation.ui.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.presentation.ui.activities.MainActivity;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_WIDGET_REALM_PREFIX;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_WIDGET_UUID_PREFIX;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_SWITCH_FRAGMENT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TODO_LIST;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_WIDGET_REPOSITORY;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

public class TodoListAppWidgetProvider extends AppWidgetProvider {

    public TodoListAppWidgetProvider() {
    }

    @SuppressLint("ApplySharedPref")
    @Override
    // TODO Delete and move to onUpdate
    public void onReceive(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        String uuid = intent.getStringExtra(KEY_UUID);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) && appWidgetId != -1 && uuid != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_PREF_WIDGET_REALM_PREFIX + appWidgetId,
                    sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM));
            editor.putString(KEY_PREF_WIDGET_UUID_PREFIX + appWidgetId, uuid);
            editor.commit();
        }
        onUpdate(context, AppWidgetManager.getInstance(context), new int[]{});
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, TodoListAppWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String realmName = sharedPreferences.getString(KEY_PREF_WIDGET_REALM_PREFIX + widgetId, FALLBACK_REALM);
            String uuid = sharedPreferences.getString(KEY_PREF_WIDGET_UUID_PREFIX + widgetId, "");
            if (!uuid.isEmpty()) {
                RemoteViews remoteViews = createRemoteViews(context, widgetId, realmName, uuid);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list_view);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove((KEY_PREF_WIDGET_REALM_PREFIX + appWidgetId));
            editor.remove(KEY_PREF_WIDGET_UUID_PREFIX + appWidgetId);
            editor.apply();
        }
        super.onDeleted(context, appWidgetIds);
    }

    private RemoteViews createRemoteViews(Context context, int appWidgetId, String repositoryName, String uuid) {

        Optional<TodoList> todoList = new TodoListRepositoryImpl(repositoryName).getTodoListById(uuid);
        String title = todoList.isPresent()
                ? todoList.get().getTitle()
                : context.getResources().getString(R.string.widget_empty);

        // Pending intent, used to start correct fragment from widget button
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_SWITCH_FRAGMENT, KEY_TODO_LIST);
        intent.putExtra(KEY_WIDGET_REPOSITORY, repositoryName);
        intent.putExtra(KEY_UUID, uuid);
        intent.putExtra(KEY_TITLE, title);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // RemoteView setup fro ListProvider
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_todo_list);
        remoteViews.setOnClickPendingIntent(R.id.widget_todo_list_bar_container, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_todo_list_bar_button, pendingIntent);
        remoteViews.setTextViewText(R.id.widget_todo_list_bar_title, title);

        // Service intent, sent to ListProvider
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.putExtra(KEY_TODO_LIST, repositoryName);
        svcIntent.putExtra(KEY_UUID, uuid);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_list_view, svcIntent);

        return remoteViews;
    }
}