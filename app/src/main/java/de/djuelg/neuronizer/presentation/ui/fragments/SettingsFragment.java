package de.djuelg.neuronizer.presentation.ui.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.preference.XpPreferenceFragment;

import de.djuelg.neuronizer.R;

import static de.djuelg.neuronizer.presentation.ui.custom.AppbarCustomizer.changeAppbarTitle;

/**
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends XpPreferenceFragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeAppbarTitle(getActivity(), R.string.settings);
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
