package de.djuelg.neuronizer.presentation.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.fragments.AddTodoListFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.PreviewFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.TodoListFragment;

import static de.djuelg.neuronizer.presentation.ui.Constants.FONT_NAME;
import static de.djuelg.neuronizer.presentation.ui.custom.AppbarFont.changeAppbarFont;

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return
        if (savedInstanceState != null) {
            return;
        }

        configurateActionBar();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, PreviewFragment.newInstance()).commit();
    }

    private void configurateActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
        changeAppbarFont(this, FONT_NAME);
    }

    @Override
    public void onTodoListSelected(String uuid, String title) {
        replaceFragment(TodoListFragment.newInstance(uuid, title));
    }

    @Override
    // TODO Replace with dialog
    public void onAddTodoList() {
        replaceFragment(new AddTodoListFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddItem() {
        // TODO implement
    }

    @Override
    public void onMarkdownHelpSelected() {
        // TODO implement
    }
}
