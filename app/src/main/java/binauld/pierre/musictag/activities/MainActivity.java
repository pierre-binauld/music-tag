package binauld.pierre.musictag.activities;

import android.app.ActionBar;
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
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.helper.LibraryItemFactoryHelper;
import binauld.pierre.musictag.io.Cache;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.LoadingState;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, ObservableScrollViewCallbacks {

    private static final int TAG_UPDATE_REQUEST = 1;

    private LibraryItemLoaderManager manager;
    private LibraryItemAdapter adapter;

    private ObservableListView listView;

    private Resources res;
    private SharedPreferences sharedPrefs;

    private LibraryItemFactory itemFactory;
    private AudioItem updating;

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
        Cache<Bitmap> cache = new Cache<>();

        // Init service(s)
        ThumbnailService thumbnailService = new ThumbnailService(cache, this, R.drawable.song);

        // Init factory
        itemFactory = LibraryItemFactoryHelper.buildFactory(this);

        // Init adapter
        adapter = new LibraryItemAdapter(thumbnailService);

        // Init progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Init manager(s)
        manager = new LibraryItemLoaderManager(adapter, itemFactory, progressBar, res.getInteger(R.integer.thumbnail_loader_update_step));

        // Load items
        switchNode(getSourceNode());

        // Init view
        listView = (ObservableListView) findViewById(R.id.library_item_list);
        listView.setOnItemClickListener(this);
        listView.setScrollViewCallbacks(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (!item.isAudioItem()) {
            FolderItem node = (FolderItem) item;
            switchNode(node);
            adapter.notifyDataSetChanged();
        } else {
            updating = (AudioItem) item;
            Intent intent = new Intent(this, TagFormActivity.class);
            intent.putExtra("file", updating.getAudioFile().getFile());
            startActivityForResult(intent, TAG_UPDATE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TAG_UPDATE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    File file = (File) extras.getSerializable("file");
                    try {
                        itemFactory.update(updating, file);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        Log.e(this.getClass().toString(), e.getMessage(), e);
                    }
                }
            }
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

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
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

        return new FolderItem(new File(sourceFolder));
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
        } else if (node.getState() == LoadingState.LOADING) {
            //Progress bar improvement: switch back the progress bar when node is loading.
        }
    }
}
