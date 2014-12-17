package binauld.pierre.musictag.fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import binauld.pierre.musictag.R;

/**
 * Fragment for the settings activity
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

}
