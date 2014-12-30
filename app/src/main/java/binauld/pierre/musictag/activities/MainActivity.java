package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.FileFilterFactory;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.helper.LibraryItemFactoryHelper;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.LoadingState;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.service.ArtworkService;
import binauld.pierre.musictag.service.CacheService;
import binauld.pierre.musictag.service.Locator;
import binauld.pierre.musictag.item.MultipleChoiceModeListenerCustom;

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
    private FileFilter filter;
    private AudioItem updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get resources
        res = getResources();
        int artworkSize = (int) res.getDimension(R.dimen.list_artwork_size);

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        // Init layout
        setContentView(R.layout.activity_main);

        // Switch off JAudioTagger log
        Logger.getLogger(res.getString(R.string.jaudiotagger_logger)).setLevel(Level.OFF);

        // Init cache
        Locator.provide(new CacheService<Bitmap>());

        // Init default decoder
        BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);

        // Init service(s)
        ArtworkService artworkService = new ArtworkService(defaultArtworkBitmapDecoder);
        artworkService.initDefaultArtwork(artworkSize);

        // Init filter
        FileFilterFactory filterFactory = new FileFilterFactory();
        filter = filterFactory.build();

        // Init factory
        itemFactory = LibraryItemFactoryHelper.buildFactory(res, filter, defaultArtworkBitmapDecoder);

        // Init progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Init adapter
        adapter = new LibraryItemAdapter(artworkService, artworkSize);
        adapter.setProgressBar(progressBar);

        // Init manager(s)
        manager = new LibraryItemLoaderManager(adapter, itemFactory, res.getInteger(R.integer.artwork_loader_update_step));

        // Load items
        switchNode(getSourceNode());

        // Init view
        listView = (ObservableListView) findViewById(R.id.library_item_list);
        listView.setOnItemClickListener(this);
        listView.setScrollViewCallbacks(this);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultipleChoiceModeListenerCustom(adapter, listView, this){

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Intent intent = adapter.sendSelection(activity);
                startActivity(intent);
            }
        });
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
            TagFormActivity.provideItem(updating);
//            intent.putExtra(TagFormActivity.AUDIO_FILE_KEY, updating.getAudioFile().getFile());
            startActivityForResult(intent, TAG_UPDATE_REQUEST);
//            File files[] = new File[1];
//            files[0]= audio.getAudioFile().getFile();
//            intent.putExtra("file", files);
//            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TAG_UPDATE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (backToParent()) {
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
//        ActionBar ab = getActionBar();
//        if (ab != null) {
//            if (scrollState == ScrollState.UP) {
//                if (ab.isShowing()) {
//                    ab.hide();
//                }
//            } else if (scrollState == ScrollState.DOWN) {
//                if (!ab.isShowing()) {
//                    ab.show();
//                }
//            }
//        }
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

        return itemFactory.buildFolderItem(new File(sourceFolder), null);
    }

    /**
     * Switch the view to the specified node.
     * If the node has not been loaded yet, then it is loaded.
     *
     * @param node The node to switch to.
     */
    private void switchNode(FolderItem node) {
        adapter.setCurrentNode(node);
        adapter.notifyDataSetChanged();
        if(null == node.getParent()) {
            setTitle(res.getString(R.string.app_name));
        } else {
            setTitle(node.getPrimaryInformation());
        }

        if (node.getState() == LoadingState.NOT_LOADED) {
            LibraryItemLoader loader = manager.get();
            manager.execute(loader, node.getFileList());
        }
    }

    /**
     * Switch the current node to the parent node.
     *
     * @return True if the adapter has switch to the parent node.
     */
    public boolean backToParent() {
        NodeItem parent = adapter.getCurrentNode().getParent();
        if (parent == null) {
            return false;
        } else {
            switchNode((FolderItem) parent);
            return true;
        }
    }
}
