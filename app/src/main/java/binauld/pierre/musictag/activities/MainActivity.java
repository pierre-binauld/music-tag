package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.helper.LoaderHelper;
import binauld.pierre.musictag.adapter.LibraryItemComparator;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity {

    private LibraryItemLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get resources
        Resources res = getResources();

        // Switch off JAudioTagger log
        Logger.getLogger(res.getString(R.string.jaudiotagger_logger)).setLevel(Level.OFF);

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sourceFolder = sharedPrefs.getString(
                res.getString(R.string.source_folder_preference_key),
                res.getString(R.string.source_folder_preference_default));

        // Init service(s)
        ThumbnailService thumbnailService = new ThumbnailService(this, R.drawable.song, R.drawable.folder);

        // Init view
        ListView listView = (ListView) findViewById(R.id.library_item_list);

        //TODO: Create a helper for adapter
        LibraryItemComparator comparator = new LibraryItemComparator();
        LibraryItemAdapter adapter = new LibraryItemAdapter(this.getBaseContext(), comparator);
        loader = LoaderHelper.buildAlphabeticalLoader(adapter, thumbnailService);

        listView.setAdapter(adapter);
        loader.execute(new File(sourceFolder));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loader.cancel(true);
    }
}
