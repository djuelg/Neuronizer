package de.djuelg.neuronizer.presentation.ui.custom;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import static de.djuelg.neuronizer.presentation.ui.Constants.FONT_NAME;

/**
 * Created by Domi on 22.07.2017.
 */

public class AppbarTitle {

    public static void changeAppbarTitle (Activity activity, @StringRes int id) {
        changeAppbarTitle(activity, activity.getResources().getString(id), FONT_NAME);
    }

    public static void changeAppbarTitle (Activity activity, String text) {
        changeAppbarTitle(activity, text, FONT_NAME);
    }

    public static void changeAppbarTitle(Activity activity, String text, String font) {
        SpannableString title = new SpannableString(text);
        title.setSpan(new TypefaceSpan(activity, font), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }
}
