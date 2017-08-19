package de.djuelg.neuronizer.presentation.ui.custom;

import android.text.Html;

/**
 * Created by Domi on 19.08.2017.
 */

public class HtmlStripper {

    public static CharSequence stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
