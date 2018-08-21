package de.djuelg.neuronizer.presentation.ui.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import net.xpece.android.support.preference.XpPreferenceFragment;

import java.util.Objects;

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
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_IMPRINT;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_INTRO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_SWITCH;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.configureAppbar;

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
        setHasOptionsMenu(true);
        permissions = new Permissions(this);
        configureAppbar(getActivity(), true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView listView = getListView();

        // We don't want this. The children are still focusable.
        listView.setFocusable(false);
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
    public void onResume() {
        super.onResume();
        changeAppbarTitle(getActivity(), R.string.settings);
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
                RadioDialogs.showRepositoryDialog(Objects.requireNonNull(getActivity()));
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

        Preference imprintPreference = this.findPreference(KEY_PREF_IMPRINT);
        imprintPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                mListener.onImprintSelected();
                return true;
            }
        });
    }
}
