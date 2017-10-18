package de.djuelg.neuronizer.presentation.ui.custom;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import de.djuelg.neuronizer.R;

/**
 * Created by djuelg on 18.10.17.
 */

public class Clipboard {

    public static void copyToClipboard(Context context, String content) {
        if (content.isEmpty()){
            Toast.makeText(context, R.string.no_clipboard, Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, content);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, R.string.added_clipboard, Toast.LENGTH_SHORT).show();
    }
}
