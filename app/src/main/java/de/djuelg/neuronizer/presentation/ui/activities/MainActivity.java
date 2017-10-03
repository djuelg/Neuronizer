package de.djuelg.neuronizer.presentation.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;
import com.rustamg.filedialogs.SaveFileDialog;

import java.io.File;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.fragments.AboutFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.ItemFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.PreviewFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.SettingsFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.TodoListFragment;
import de.djuelg.neuronizer.presentation.ui.widget.TodoListAppWidgetProvider;
import de.djuelg.neuronizer.storage.RepositoryManager;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_PREVIEW_INTRO_SHOWN;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_TODO_LIST_INTRO_SHOWN;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_SWITCH_FRAGMENT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TODO_LIST;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_WIDGET_REPOSITORY;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener, FileDialog.OnFileSelectedListener {

    private SharedPreferences sharedPreferences;

    public static Intent newInstace(AppCompatActivity activity, String type, String uuid, String title) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(KEY_SWITCH_FRAGMENT, type);
        intent.putExtra(KEY_UUID, uuid);
        intent.putExtra(KEY_TITLE, title);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        configurateActionBar();

        if (savedInstanceState == null) { // not on minimize/maximize

            // Check if we have to show intro slides
            boolean shown = sharedPreferences.getBoolean(KEY_PREF_PREVIEW_INTRO_SHOWN, false);
            if (!shown) {
                startActivity(IntroActivity.previewIntroInstance(this));
                finish();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, PreviewFragment.newInstance()).commit();

            switchFragmentBasedOnIntent();
        }
    }

    private void switchFragmentBasedOnIntent() {
        // Check if we are coming from IntroActivity
        Intent intent = getIntent();
        String type = intent.getStringExtra(KEY_SWITCH_FRAGMENT);
        if (KEY_TODO_LIST.equals(type)) {
            String activeRepository = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
            String widgetRepositoryName = intent.getStringExtra(KEY_WIDGET_REPOSITORY);
            String uuid = intent.getStringExtra(KEY_UUID);
            String title = intent.getStringExtra(KEY_TITLE);
            if (uuid == null || title == null) return;
            if (widgetRepositoryName != null && !activeRepository.equals(widgetRepositoryName)) return;
            onTodoListSelected(uuid, title);
        }
    }

    private void configurateActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onUpdateAllWidgets(75);
    }

    @Override
    public void onTodoListSelected(String uuid, String title) {
        // Check if we have to show intro slides
        boolean shown = sharedPreferences.getBoolean(KEY_PREF_TODO_LIST_INTRO_SHOWN, false);
        if (shown) {
            replaceFragment(TodoListFragment.newInstance(uuid, title));
        } else {
            startActivity(IntroActivity.todoListIntroInstance(this, uuid, title));
            finish();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddItem(String todoListUuid) {
        replaceFragment(ItemFragment.addItem(todoListUuid));
    }

    @Override
    public void onEditItem(String todoListUuid, String itemUuid) {
        replaceFragment(ItemFragment.editItem(todoListUuid, itemUuid));
    }

    @Override
    public void onSettingsSelected() {
        replaceFragment(SettingsFragment.newInstance());
    }

    @Override
    public void onAboutSelected() {
        replaceFragment(AboutFragment.newInstance());
    }

    @Override
    public void onUpdateAllWidgets(int delayMillis) {
        TodoListAppWidgetProvider.sendRefreshBroadcastDelayed(this, delayMillis);
    }

    @Override
    public void onFileSelected(FileDialog dialog, File file) {
        boolean success;
        if (dialog instanceof OpenFileDialog) {
            success = RepositoryManager.importRepository(getApplicationContext().getFilesDir(), file);
            Toast.makeText(this, success ? R.string.import_success : R.string.import_failure, Toast.LENGTH_SHORT).show();
        } else if (dialog instanceof SaveFileDialog) {
            String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
            success = RepositoryManager.exportRepository(file, repositoryName);
            Toast.makeText(this, success ? R.string.export_success : R.string.export_failure, Toast.LENGTH_SHORT).show();
        }
    }
}
