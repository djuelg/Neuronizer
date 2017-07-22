package de.djuelg.neuronizer.presentation.ui.custom;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import de.djuelg.neuronizer.R;

/**
 * Created by Domi on 22.07.2017.
 */

public class AppbarFont {

    public static void changeAppbarFont(AppCompatActivity activity, String font) {
        SpannableString title = new SpannableString(activity.getResources().getString(R.string.app_name));
        title.setSpan(new TypefaceSpan(activity, font), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }
}
