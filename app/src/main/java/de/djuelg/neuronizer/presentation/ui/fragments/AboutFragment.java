package de.djuelg.neuronizer.presentation.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import jp.wasabeef.richeditor.RichEditor;

import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_ABOUT_CSS;
import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_FONT_SIZE;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;

public class AboutFragment extends Fragment {

    @Bind(R.id.richEditor_about) RichEditor richEditor;
    @Bind(R.id.about_link) TextView aboutLink;

    public AboutFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        changeAppbarTitle(getActivity(), R.string.about);

        richEditor.setHtml(getString(R.string.about_html));
        richEditor.loadCSS(EDITOR_ABOUT_CSS);
        richEditor.setPadding(24, 0, 24, 0);
        richEditor.setEditorFontSize(EDITOR_FONT_SIZE);
        richEditor.setInputEnabled(false);
        richEditor.setFocusable(false);

        aboutLink.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
