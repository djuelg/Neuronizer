package de.djuelg.neuronizer.presentation.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.fragments.AddTodoListFragment;
import de.djuelg.neuronizer.presentation.ui.fragments.PreviewFragment;

public class MainActivity extends AppCompatActivity implements PreviewFragment.OnInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return
        if (savedInstanceState != null) {
            return;
        }

        PreviewFragment fragment = new PreviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onTodoListSelected(String uuid, String title) {
        // Start Fragment with given TodoList
    }

    @Override
    public void onAddTodoList() {
        replaceFragment(new AddTodoListFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
