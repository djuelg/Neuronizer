package de.djuelg.neuronizer.presentation.ui.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_WIDGET_REALM_PREFIX;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_WIDGET_UUID_PREFIX;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

public class TodoListAppWidgetConfigure extends Activity {

    private TodoList[] todoLists;
    private RadioButton[] radioButtons;
    private RadioGroup radioGroup;
    private String repositoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget_configure);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        List<TodoList> list = new TodoListRepositoryImpl(repositoryName).getTodoLists();

        radioGroup = findViewById(R.id.widget_config_radio_group);
        todoLists = list.toArray(new TodoList[list.size()]);
        radioButtons = new RadioButton[todoLists.length];
        createRadioButtons();
    }

    private void createRadioButtons() {
        for (int i=0; i < todoLists.length; i++) {
            radioButtons[i] = new RadioButton(this);
            radioButtons[i].setText(todoLists[i].getTitle());
            radioGroup.addView(radioButtons[i]);
        }
        if (todoLists.length > 0) {
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        } else {
            cancelWidgetCreation();
        }
    }

    // Called from xml
    public void selectButtonClicked(View view) {
        for (int i=0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                createAndUpdateWidget(todoLists[i].getUuid());
            }
        }
    }

    // Called from xml
    public void cancelButtonClicked(View view) {
        cancelWidgetCreation();
    }

    public void createAndUpdateWidget(String uuid) {
        int appWidgetId = getAppWidgetIdFromIntent();
        if( appWidgetId != -1) {
            // save new widget
            saveWidgetToSharedPrefs(appWidgetId, uuid);

            // update widget view
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            TodoListAppWidgetProvider.updateAppWidget(manager, this, appWidgetId, repositoryName, uuid);

            // finish
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
        finish();
    }

    private void saveWidgetToSharedPrefs(int appWidgetId, String uuid) {
        // add widget id's to sharedPrefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PREF_WIDGET_REALM_PREFIX + appWidgetId,
                sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM));
        editor.putString(KEY_PREF_WIDGET_UUID_PREFIX + appWidgetId, uuid);
        editor.apply();
    }

    private int getAppWidgetIdFromIntent() {
        Intent arrivedIntent = getIntent();
        if (arrivedIntent.getExtras() != null) {
             return arrivedIntent.getExtras().getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        return AppWidgetManager.INVALID_APPWIDGET_ID;
    }

    private void cancelWidgetCreation() {
        finish();
    }
}
