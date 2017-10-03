package de.djuelg.neuronizer.presentation.ui.widget;

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

    public static void sendRefreshBroadcastDelayed(Context context, int delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        sendRefreshBroadcast(context);
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(context, TodoListAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context, TodoListAppWidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, manager.getAppWidgetIds(component));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName component = new ComponentName(context, TodoListAppWidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(component), R.id.widget_list_view);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        for (int appWidgetId : appWidgetIds) {
            String realmName = sharedPreferences.getString(KEY_PREF_WIDGET_REALM_PREFIX + appWidgetId, FALLBACK_REALM);
            String uuid = sharedPreferences.getString(KEY_PREF_WIDGET_UUID_PREFIX + appWidgetId, "");
            updateAppWidget(appWidgetManager, context, appWidgetId, realmName, uuid);
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

    public static void updateAppWidget(AppWidgetManager manager, Context context, int appWidgetId, String repositoryName, String uuid) {
        RemoteViews views = createRemoteViews(context, appWidgetId, repositoryName, uuid);

        // Service intent, sent to WidgetListFactory
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.putExtra(KEY_TODO_LIST, repositoryName);
        svcIntent.putExtra(KEY_UUID, uuid);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_list_view, svcIntent);
        manager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews createRemoteViews(Context context, int appWidgetId, String repositoryName, String uuid) {
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

        // RemoteView setup fro WidgetListFactory
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_todo_list);
        remoteViews.setOnClickPendingIntent(R.id.widget_todo_list_bar_container, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_todo_list_bar_button, pendingIntent);
        remoteViews.setTextViewText(R.id.widget_todo_list_bar_title, title);

        return remoteViews;
    }
}