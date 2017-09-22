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
import de.djuelg.neuronizer.presentation.ui.Constants;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

public class TodoListAppWidgetConfigure extends Activity {

    private TodoList[] todoLists;
    private RadioButton[] radioButtons;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
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
                createResultIntent(todoLists[i].getUuid());
            }
        }
    }

    // Called from xml
    public void cancelButtonClicked(View view) {
        cancelWidgetCreation();
    }

    public void createResultIntent(String uuid) {
        // TODO Setup intent correct like in https://developer.android.com/guide/topics/appwidgets/index.html#Configuring
        int appWidgetId = getAppWidgetIdFromIntent();

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, TodoListAppWidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(Constants.KEY_UUID, uuid);
        sendBroadcast(intent);
        setResult(RESULT_OK, intent);
        finish();
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
        setResult(RESULT_CANCELED);
        finish();
    }
}
