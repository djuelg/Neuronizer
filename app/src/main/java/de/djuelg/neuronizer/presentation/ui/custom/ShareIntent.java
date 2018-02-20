package de.djuelg.neuronizer.presentation.ui.custom;

import android.content.Context;
import android.content.Intent;

import com.fernandocejas.arrow.strings.Strings;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListItemViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import static de.djuelg.neuronizer.presentation.ui.custom.MarkdownConverter.convertToMarkdown;

/**
 * Created by Domi on 24.08.2017.
 */

public class ShareIntent {

    private StringBuilder content;

    private ShareIntent(String title) {
        this.content = new StringBuilder().append("# ").append(title).append(" #\n");
    }

    public static ShareIntent withTitle(String title) {
        return new ShareIntent(title);
    }


    public ShareIntent withItems(List<AbstractFlexibleItem> items) {
        appendMarkdownListFrom(items);
        return this;
    }

    public ShareIntent withHtml(String html) {
        String markdown = convertToMarkdown(html);
        content.append(markdown);
        return this;
    }

    public void send(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content.toString());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.share)));
    }

    private void appendMarkdownListFrom(List<AbstractFlexibleItem> items) {
        for (AbstractFlexibleItem item : items) {

            if (item instanceof TodoListHeaderViewModel) {
                content.append("\n## ").append(((TodoListHeaderViewModel) item).getHeader().getTitle()).append(" ##\n");
            } else {
                content.append("- ").append(((TodoListItemViewModel) item).getItem().getTitle()).append("\n");
                if (((TodoListItemViewModel) item).getItem().hasDetails()) {
                    addDetails(((TodoListItemViewModel) item).getItem().getDetails());
                }
            }
        }
    }

    private void addDetails(String details) {
        String markdownDetails = convertToMarkdown(details);
        String detailLines[] = markdownDetails.split("\\r?\\n");
        List<String> indentedLines = new ArrayList<>(detailLines.length);
        for (String line : detailLines) {
            indentedLines.add(String.format("    %s", line));
        }
        content.append(Strings.joinOn("\n").join(indentedLines)).append("\n");
    }
}
