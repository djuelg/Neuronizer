package de.djuelg.neuronizer.presentation.ui.custom.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import de.djuelg.neuronizer.R;

import static de.djuelg.neuronizer.presentation.ui.Constants.FONT_NAME;

/**
 * Created by Domi on 22.07.2017.
 */

public class AppbarCustomizer {

    public static void configureAppbar(Activity activity, boolean showBackArrow) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(!showBackArrow);
            actionBar.setDisplayShowHomeEnabled(!showBackArrow);
            actionBar.setDisplayHomeAsUpEnabled(showBackArrow);
            if (!showBackArrow) actionBar.setIcon(R.mipmap.ic_launcher);
        }
    }

    public static void changeAppbarColor(Activity activity, @ColorRes int id) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(id)));
    }

    public static void changeAppbarTitle (Activity activity, @StringRes int id) {
        changeAppbarTitle(activity, activity.getResources().getString(id));
    }

    public static void changeAppbarTitle(Activity activity, String text) {
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(fontifyString(activity, text));
    }

    public static SpannableString fontifyString(Context context, String text) {
        SpannableString string = new SpannableString(text);
        string.setSpan(new TypefaceSpan(context, FONT_NAME), 0, string.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
}
