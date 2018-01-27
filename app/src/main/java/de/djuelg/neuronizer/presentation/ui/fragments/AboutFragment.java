package de.djuelg.neuronizer.presentation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.djuelg.neuronizer.R;
import jp.wasabeef.richeditor.RichEditor;

import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_ABOUT_CSS;
import static de.djuelg.neuronizer.presentation.ui.Constants.EDITOR_FONT_SIZE;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.configureAppbar;

public class AboutFragment extends Fragment {

    @BindView(R.id.richEditor_about) RichEditor richEditor;
    @BindView(R.id.about_link) TextView aboutLink;
    private Unbinder mUnbinder;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        configureAppbar(getActivity(), true);
        changeAppbarTitle(getActivity(), R.string.about);
        setHasOptionsMenu(true);

        richEditor.setHtml(getString(R.string.about_html));
        richEditor.loadCSS(EDITOR_ABOUT_CSS);
        richEditor.setPadding(24, 0, 24, 0);
        richEditor.setEditorFontSize(EDITOR_FONT_SIZE);
        richEditor.setInputEnabled(false);
        richEditor.setFocusable(false);

        aboutLink.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
