package de.djuelg.neuronizer.presentation.ui.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.XpPreferenceFragment;

import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.custom.Permissions;
import de.djuelg.neuronizer.presentation.ui.dialog.FileDialogs;
import de.djuelg.neuronizer.presentation.ui.dialog.RadioDialogs;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ABOUT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_EXPORT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_IMPORT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_INTRO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_SWITCH;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;

/**
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends XpPreferenceFragment {

    private Permissions permissions;
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
        permissions = new Permissions(getActivity());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        String permission = this.permissions.getGrantedPermission(requestCode, permissions, grantResults);
        switch (permission) {
            case READ_EXTERNAL_STORAGE:
                FileDialogs.showOpenFileDialog(getActivity());
                break;
            case WRITE_EXTERNAL_STORAGE:
                FileDialogs.showSaveFileDialog(getActivity());
                break;
        }
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        Preference switchPreference = this.findPreference(KEY_PREF_SWITCH);
        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                RadioDialogs.showRepositoryDialog(getActivity());
                return true;
            }
        });

        Preference importPreference = this.findPreference(KEY_PREF_IMPORT);
        importPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                boolean necessary = permissions.requestPermissionIfNecessary(READ_EXTERNAL_STORAGE);
                if (!necessary) {
                    FileDialogs.showOpenFileDialog(getActivity());
                    // else handle in callback method
                }
                return true;
            }
        });

        Preference exportPreference = this.findPreference(KEY_PREF_EXPORT);
        exportPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                boolean necessary = permissions.requestPermissionIfNecessary(READ_EXTERNAL_STORAGE);
                if (!necessary) {
                    FileDialogs.showSaveFileDialog(getActivity());
                    // else handle in callback method
                }
                return true;
            }
        });

        Preference introPreference = this.findPreference(KEY_PREF_INTRO);
        introPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                RadioDialogs.showIntroDialog((AppCompatActivity) getActivity());
                return true;
            }
        });

        Preference aboutPreference = this.findPreference(KEY_PREF_ABOUT);
        aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                mListener.onAboutSelected();
                return true;
            }
        });
    }
}
