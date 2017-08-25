package de.djuelg.neuronizer.presentation.ui.custom.view;

import android.view.View;

import de.djuelg.neuronizer.R;
import jp.wasabeef.richeditor.RichEditor;

import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_DETAILS_CSS;
import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_FONT_SIZE;
import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_HEIGHT;
import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_PADDING;

/**
 * Created by Domi on 18.08.2017.
 */

public class RichEditorNavigation {

    private final View mView;
    private final RichEditor mEditor;

    public RichEditorNavigation(View view, RichEditor mEditor) {
        this.mView = view;
        this.mEditor = mEditor;
    }

    public static void setupRichEditor(RichEditor richEditor) {
        richEditor.setEditorHeight(EDITOR_HEIGHT);
        richEditor.setPadding(EDITOR_PADDING, EDITOR_PADDING, EDITOR_PADDING, EDITOR_PADDING);
        richEditor.setEditorFontSize(EDITOR_FONT_SIZE);
        richEditor.loadCSS(EDITOR_DETAILS_CSS);
    }

    public void setupRichEditor() {
        setupRichEditor(mEditor);
    }

    public void setupOnClickListeners() {
        final View richTextNavigation = mView.findViewById(R.id.richtext_navigation);

        mEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    richTextNavigation.setVisibility(View.VISIBLE);
                } else {
                    richTextNavigation.setVisibility(View.GONE);
                }
            }
        });

        mView.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        mView.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                v.setSelected(!v.isSelected());
                mEditor.setBold();
            }
        });

        mView.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                v.setSelected(!v.isSelected());
                mEditor.setItalic();
            }
        });

        mView.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                v.setSelected(!v.isSelected());
                mEditor.setUnderline();
            }
        });

        mView.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        mView.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        mView.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        mView.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        mView.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        mView.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        mView.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        mView.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        mView.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        mView.findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });
    }
}
