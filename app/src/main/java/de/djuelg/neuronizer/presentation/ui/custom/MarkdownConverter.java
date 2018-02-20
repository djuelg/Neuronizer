package de.djuelg.neuronizer.presentation.ui.custom;

import com.overzealous.remark.Options;
import com.overzealous.remark.Remark;

/**
 * Created by djuelg on 20.02.18.
 */

public class MarkdownConverter {

    public static String convertToMarkdown(String html) {
        Remark remark = new Remark(Options.github());
        return remark.convert((html != null) ? html : "");
    }
}
