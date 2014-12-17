package binauld.pierre.musictag.activities;


import android.os.Bundle;
import android.preference.PreferenceActivity;

import binauld.pierre.musictag.fragments.SettingsFragment;

/**
 * Activity for application settings
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
}
