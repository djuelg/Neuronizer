package de.djuelg.neuronizer.presentation.ui.custom;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListItemViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by Domi on 24.08.2017.
 */

public class ShareIntent {

    private String title;
    private List<AbstractFlexibleItem> items;

    private ShareIntent(String title) {
        this.title = title;
    }

    public static ShareIntent withTitle(String title) {
        return new ShareIntent(title);
    }


    public ShareIntent withItems(List<AbstractFlexibleItem> items) {
        this.items = items;
        return this;
    }

    public void send(Context context) {
        if (title == null || items == null) throw new IllegalStateException("All fields must be set");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, buildMarkdownList(title, items));
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.share)));
    }

    private String buildMarkdownList(String title, List<AbstractFlexibleItem> items) {
        StringBuilder content = new StringBuilder();
        content.append("# ").append(title).append("\n");

        for (AbstractFlexibleItem item : items) {

            if (item instanceof TodoListHeaderViewModel) {
                content.append("\n## ").append(((TodoListHeaderViewModel) item).getHeader().getTitle()).append("\n");
            } else {
                content.append("- ").append(((TodoListItemViewModel) item).getItem().getTitle()).append("\n");
            }
        }
        return content.toString();
    }
}
