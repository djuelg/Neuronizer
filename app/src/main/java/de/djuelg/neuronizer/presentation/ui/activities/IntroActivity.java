package de.djuelg.neuronizer.presentation.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import java.util.List;

import de.djuelg.neuronizer.presentation.ui.custom.SliderPageFactory;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_INTRO_PREVIEW;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_INTRO_TODO_LIST;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_INTRO_TYPE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_PREVIEW_INTRO_SHOWN;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_TODO_LIST_INTRO_SHOWN;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;

/**
 * Created by djuelg on 13.09.17.
 *
 * Note here that we DO NOT use setContentView();
 */

public class IntroActivity extends AppIntro2 {

    private String type;
    private String uuid;
    private String title;

    public static Intent previewIntroInstance(AppCompatActivity activity) {
        Intent intent = new Intent(activity, IntroActivity.class);
        intent.putExtra(KEY_INTRO_TYPE, KEY_INTRO_PREVIEW);
        return intent;
    }

    public static Intent todoListIntroInstance(AppCompatActivity activity, @Nullable String uuid, @Nullable String title) {
        Intent intent = new Intent(activity, IntroActivity.class);
        intent.putExtra(KEY_INTRO_TYPE, KEY_INTRO_TODO_LIST);
        if (uuid != null) intent.putExtra(KEY_UUID, uuid);
        if (title != null) intent.putExtra(KEY_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSkipButton(false);

        Intent intent = getIntent();
        type = intent.getStringExtra(KEY_INTRO_TYPE);
        uuid = intent.getStringExtra(KEY_UUID);
        title = intent.getStringExtra(KEY_TITLE);

        SliderPageFactory factory = new SliderPageFactory(this);
        List<SliderPage> pages = factory.createSlidesFromType(type);
        for (SliderPage page : pages) {
            addSlide(AppIntroFragment.newInstance(page));
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Not usable
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case KEY_INTRO_PREVIEW:
                editor.putBoolean(KEY_PREF_PREVIEW_INTRO_SHOWN, true);
                break;
            case KEY_INTRO_TODO_LIST:
                editor.putBoolean(KEY_PREF_TODO_LIST_INTRO_SHOWN, true);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown type %s", type));
        }
        editor.apply();

        startActivity(MainActivity.newInstace(this, type, uuid, title));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}