package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.LibraryComponentAdapter;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.helper.LibraryComponentFactoryHelper;
import binauld.pierre.musictag.listener.ItemMultiChoiceMode;
import binauld.pierre.musictag.loader.LibraryComponentLoader;
import binauld.pierre.musictag.loader.LibraryComponentLoaderManager;
import binauld.pierre.musictag.util.SharedObject;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LoadingState;
import binauld.pierre.musictag.loader.AsyncTaskExecutor;
import binauld.pierre.musictag.service.ArtworkService;
import binauld.pierre.musictag.service.CacheService;
import binauld.pierre.musictag.service.Locator;
import binauld.pierre.musictag.wrapper.FileWrapper;
import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, ObservableScrollViewCallbacks {

    private static final int TAG_UPDATE_REQUEST = 1;

    private LibraryComponentLoaderManager manager;
    private LibraryComponentAdapter adapter;

    private ObservableListView listView;

    private FileWrapper wrapper;

    private OnItemClickVisitor onItemClickVisitor;

    private Resources res;
    private SharedPreferences sharedPrefs;

    private LibraryComponentFactory componentFactory;

    private ItemMultiChoiceMode ItemMultiChoiceMode;
//    private List<LibraryItem> updating;

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

        // Init Wrapper
        wrapper = new JAudioTaggerWrapper();

        // Init visitor
        onItemClickVisitor = new OnItemClickVisitor();

        // Init factory
        componentFactory = LibraryComponentFactoryHelper.buildFactory(res, wrapper, defaultArtworkBitmapDecoder);

        // Init progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Init adapter
        adapter = new LibraryComponentAdapter(artworkService, artworkSize);
        adapter.setProgressBar(progressBar);

        // Init manager(s)
        manager = new LibraryComponentLoaderManager(componentFactory, res.getInteger(R.integer.artwork_loader_update_step));

        // Load items
        switchComposite(getSourceComposite());

        // Init view
        listView = (ObservableListView) findViewById(R.id.list_library_item);
        listView.setOnItemClickListener(this);
        listView.setScrollViewCallbacks(this);
        listView.setAdapter(adapter);

        // Init multi choice mode listener
        ItemMultiChoiceMode = new ItemMultiChoiceMode(adapter, listView, this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(ItemMultiChoiceMode);
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
                callSettingsActivity();
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
        LibraryComponent component = (LibraryComponent) adapterView.getItemAtPosition(i);
        component.accept(onItemClickVisitor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TAG_UPDATE_REQUEST) {
            // Make sure the request was successful
            switch (resultCode) {
                case RESULT_OK:
                    listView.clearChoices();
                    listView.setItemChecked(-1, false);
                    ItemMultiChoiceMode.clearSelection();
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
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
            switchComposite(getSourceComposite());
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


    private void callSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void callTagFormActivity(List<LibraryComponent> components) {
        Intent intent = new Intent(this, TagFormActivity.class);
        SharedObject.provideComponent(components);
        SharedObject.provideComponentFactory(componentFactory);
        startActivityForResult(intent, TAG_UPDATE_REQUEST);
    }

    /**
     * Get the source folder item from shared preferences.
     *
     * @return The source folder item.
     */
    public LibraryComposite getSourceComposite() {
        String sourceFolder = sharedPrefs.getString(
                res.getString(R.string.source_folder_preference_key),
                res.getString(R.string.source_folder_preference_default));

        return componentFactory.buildComposite(new File(sourceFolder), null);
    }

    /**
     * Switch the view to the specified node.
     * If the node has not been loaded yet, then it is loaded.
     *
     * @param composite The composite to switch to.
     */
    private void switchComposite(LibraryComposite composite) {
        adapter.setCurrentComposite(composite);
        adapter.notifyDataSetChanged();
        if (null == composite.getParent()) {
            setTitle(res.getString(R.string.app_name));
        } else {
            setTitle(composite.getItem().getPrimaryInformation());
        }

        if (composite.getState() == LoadingState.NOT_LOADED) {
            LibraryComponentLoader loader = manager.get(false, new LibraryComponentLoader.Callback() {

                @Override
                public void onProgressUpdate(LibraryComposite libraryComposite) {
                    if (adapter.getCurrentComposite().equals(libraryComposite)) {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onPostExecute(/*List<LibraryComposite> items*/) {
                }
            });

            AsyncTaskExecutor.execute(loader, composite);
        }
    }

    /**
     * Switch the current node to the parent node.
     *
     * @return True if the adapter has switch to the parent node.
     */
    public boolean backToParent() {
        LibraryComposite parent = adapter.getCurrentComposite().getParent();
        if (parent == null) {
            return false;
        } else {
            switchComposite( parent);
            return true;
        }
    }

    class OnItemClickVisitor implements ComponentVisitor {

        @Override
        public void visit(LibraryLeaf leaf) {
            List<LibraryComponent> leaves = new ArrayList<>();
            leaves.add(leaf);
            callTagFormActivity(leaves);
        }

        @Override
        public void visit(LibraryComposite composite) {
            switchComposite(composite);
            adapter.notifyDataSetChanged();
        }
    }
}
