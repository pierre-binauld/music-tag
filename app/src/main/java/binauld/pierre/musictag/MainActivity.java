package binauld.pierre.musictag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.helper.LoaderHelper;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Switch off JAudioTagger log
        //TODO: put it into res
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //TODO: Put source folder into res
        String sourceFolder = sharedPrefs.getString(SettingsActivity.KEY_SOURCE_FOLDER, "");

        // Init service(s)
        ThumbnailService thumbnailService = new ThumbnailService(this, R.drawable.song, R.drawable.folder);

        ListView listView = (ListView) findViewById(R.id.library_item_list);

        LibraryItemAdapter adapter = new LibraryItemAdapter(this.getBaseContext());
        LibraryItemLoader loader = LoaderHelper.buildAlphabeticalLoader(adapter, thumbnailService);

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
        int id = item.getItemId();
        boolean result = false;

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
