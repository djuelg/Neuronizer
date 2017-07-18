package de.djuelg.neuronizer.presentation.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.custom.TypefaceSpan;
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

        changeAppbarFont();

        PreviewFragment fragment = new PreviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
    }

    // TODO set appbar icon
    private void changeAppbarFont() {
        SpannableString title = new SpannableString(getResources().getString(R.string.app_name));
        title.setSpan(new TypefaceSpan(this, getResources().getString(R.string.appbar_font)), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
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
