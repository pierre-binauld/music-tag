package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.io.Cache;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.adapter.LibraryItemComparator;
import binauld.pierre.musictag.helper.AdapterHelper;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.item.LoadingState;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private LibraryItemLoaderManager manager;
    private LibraryItemAdapter adapter;

    private Resources res;
    private SharedPreferences sharedPrefs;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get resources
        res = getResources();

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        // Init theme
        setContentView(R.layout.activity_main);


        // Switch off JAudioTagger log
        Logger.getLogger(res.getString(R.string.jaudiotagger_logger)).setLevel(Level.OFF);

        // Init cache
        Cache<Bitmap> cache = new Cache<Bitmap>();

        // Init service(s)
        ThumbnailService thumbnailService = new ThumbnailService(cache, this, R.drawable.song, R.drawable.folder);

        // Init adapter
        adapter = AdapterHelper.buildAdapter(this.getBaseContext(), thumbnailService, new LibraryItemComparator());

        // Init progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
//        progressBar.setProgress(50);

        // Init manager(s)
        manager = new LibraryItemLoaderManager(adapter, thumbnailService, progressBar);

        // Load items
        switchNode(getSourceNode());

        // Init view
        listView = (ListView) findViewById(R.id.library_item_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

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
        if (!item.getAudio()) {
            FolderItem node = (FolderItem) item;
            //TODO: Load image when they are displayed
            switchNode(node);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (adapter.backToParent()) {
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(res.getString(R.string.source_folder_preference_key))) {
            switchNode(getSourceNode());
        }
    }

    /**
     * Get the source folder item from shared preferences.
     *
     * @return The source folder item.
     */
    public FolderItem getSourceNode() {
        String sourceFolder = sharedPrefs.getString(
                res.getString(R.string.source_folder_preference_key),
                res.getString(R.string.source_folder_preference_default));

        return new FolderItem(new File(sourceFolder), new LibraryItemComparator());
    }

    /**
     * Switch the view to the specified node.
     * If the node has not been loaded yet, then it is loaded.
     *
     * @param node The node to switch to.
     */
    private void switchNode(FolderItem node) {
        adapter.setCurrentNode(node);
        if (node.getState() == LoadingState.NOT_LOADED) {
            LibraryItemLoader loader = manager.get();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, node);
            } else {
                loader.execute(node);
            }
//          node.setIsLoaded(true);
        } else if (node.getState() == LoadingState.LOADING) {
            //TODO: Progress bar improvement: switch the progress bar when node is loading.
        }
    }
}
