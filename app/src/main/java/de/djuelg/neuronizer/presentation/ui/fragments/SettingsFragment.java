package de.djuelg.neuronizer.presentation.ui.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.preference.Preference;
import android.support.v7.preference.XpPreferenceFragment;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ABOUT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_INTRO;
import static de.djuelg.neuronizer.presentation.ui.custom.AppbarCustomizer.changeAppbarTitle;

/**
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends XpPreferenceFragment {

    private FragmentInteractionListener mListener;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeAppbarTitle(getActivity(), R.string.settings);
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        Preference introPreference = this.findPreference(KEY_PREF_INTRO);
        introPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                getActivity().getSupportFragmentManager().popBackStack();
                mListener.onIntroSelected();
                return true;
            }
        });

        Preference aboutPreference = this.findPreference(KEY_PREF_ABOUT);
        aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                getActivity().getSupportFragmentManager().popBackStack();
                mListener.onAboutSelected();
                return true;
            }
        });
    }
}
