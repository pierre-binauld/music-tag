package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.FolderItem;
import binauld.pierre.musictag.adapter.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.adapter.LibraryItemComparator;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private LibraryItemLoaderManager manager;
    private LibraryItemAdapter adapter;

    private Resources res;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: create progress bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get resources
        res = getResources();

        // Switch off JAudioTagger log
        Logger.getLogger(res.getString(R.string.jaudiotagger_logger)).setLevel(Level.OFF);

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Init service(s)
        ThumbnailService thumbnailService = new ThumbnailService(this, R.drawable.song, R.drawable.folder);


        //TODO: Create a helper for adapter
        LibraryItemComparator comparator = new LibraryItemComparator();
        adapter = new LibraryItemAdapter(this.getBaseContext(), comparator);
        adapter.setCurrentNode(getSourceNode());

        // Init manager(s)
        manager = new LibraryItemLoaderManager(adapter, thumbnailService);
        LibraryItemLoader loader = manager.get();


        // Init view
        ListView listView = (ListView) findViewById(R.id.library_item_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        // Load items
        loader.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf(this.getClass().toString(), "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.wtf(this.getClass().toString(), "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf(this.getClass().toString(), "onResume");
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
        manager.cancelAll(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LibraryItem item = (LibraryItem) adapterView.getItemAtPosition(i);
        if (!item.isSong()) {
            FolderItem node = (FolderItem) item;
            adapter.setCurrentNode(node);
            if (!node.isLoaded()) {
                //TODO: Why a loader wait for the previous has finished ?
                //TODO: Load image when they are displayed
                manager.get().execute();
                node.setIsLoaded(true);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if(adapter.backToParent()) {
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public FolderItem getSourceNode() {
        //TODO: When source folder settings change, reload list view
        String sourceFolder = null;
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            //TODO: Put string in res
            sourceFolder = extras.getString("source_folder");
        }

        if (null == sourceFolder) {
            sourceFolder = sharedPrefs.getString(
                    res.getString(R.string.source_folder_preference_key),
                    res.getString(R.string.source_folder_preference_default));
        }

        return new FolderItem(new File(sourceFolder), new LibraryItemComparator());
    }
}
