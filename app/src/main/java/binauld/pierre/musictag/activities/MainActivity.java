package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.LibraryComponentAdapter;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.composite.LoadingState;
import binauld.pierre.musictag.listener.ItemMultiChoiceMode;
import binauld.pierre.musictag.service.ArtworkManager;
import binauld.pierre.musictag.service.LibraryService;
import binauld.pierre.musictag.service.LibraryServiceImpl;
import binauld.pierre.musictag.service.state.LibraryServiceState;
import binauld.pierre.musictag.visitor.ComponentVisitor;

/**
 * Main activity of the app.
 * Display a list of directories and audio files the user can modify.
 */
public class MainActivity extends Activity implements ServiceConnection, AdapterView.OnItemClickListener, ObservableScrollViewCallbacks {

    private static final int TAG_UPDATE_REQUEST = 1;
    private static final int ORGANISATION_REQUEST = 2;

    private LibraryService service;
    private LibraryServiceState serviceState;

    private LibraryComponentAdapter adapter;

    private ObservableListView listView;
    private ProgressBar progressBar;

    private OnItemClickVisitor onItemClickVisitor;

    private Resources res;
    private SharedPreferences sharedPrefs;

    private ItemMultiChoiceMode ItemMultiChoiceMode;
//    private List<LibraryItem> updating;

    private Runnable updateListViewCallback = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateListView();
                    updateProgressBar();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get resources
        res = getResources();

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Init layout
        setContentView(R.layout.activity_main);

        // Switch off JAudioTagger log
        Logger.getLogger(res.getString(R.string.jaudiotagger_logger)).setLevel(Level.OFF);


        // Init default decoder
//        BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);


        // Init Wrapper
//        wrapper = new JAudioTaggerWrapper();

        // Init visitor
        onItemClickVisitor = new OnItemClickVisitor();

        // Init factory
//        componentFactory = LibraryComponentFactoryHelper.buildFactory(res, wrapper, defaultArtworkBitmapDecoder);

        // Init progress bar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Init manager(s)
//        manager = new LibraryComponentLoaderManager(componentFactory, res.getInteger(R.integer.library_loader_update_step));

        // Init Title
        initTitle();

        // Init view
        listView = (ObservableListView) findViewById(R.id.list_library_item);
        listView.setOnItemClickListener(this);
        listView.setScrollViewCallbacks(this);

        // Init service
        Intent intent = new Intent(this, LibraryServiceImpl.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

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
            case R.id.action_organisation:
//                callOrganisationActivity(adapter.getComposite());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != service) {
            serviceState.loadCurrentComposite(false, updateListViewCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != service) {
            unbindService(this);
        }
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
        } else if (requestCode == ORGANISATION_REQUEST) {
            // Make sure the request was successful
            switch (resultCode) {
                case RESULT_OK:
//                        LibraryComposite composite = getSourceComposite();
//                        switchComposite(composite);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backToParent()) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        LibraryServiceImpl.LibraryServiceBinder binder = (LibraryServiceImpl.LibraryServiceBinder) service;
        this.service = binder.getService();
        this.serviceState = this.service.getServiceState();

        this.serviceState.loadCurrentComposite(false, updateListViewCallback);
        this.updateTitle();
        this.initProgressBar();


        // Init artwork manager
        int artworkSize = (int) res.getDimension(R.dimen.list_artwork_size);
        ArtworkManager artworkManager = this.service.getArtworkManager();
        artworkManager.initDefaultArtwork(artworkSize);

        // Init adapter
        this.adapter = new LibraryComponentAdapter(artworkManager, artworkSize);
        this.listView.setAdapter(this.adapter);

        // Init multi choice mode listener
        this.ItemMultiChoiceMode = new ItemMultiChoiceMode(this.adapter, this.listView, this);
        this.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        this.listView.setMultiChoiceModeListener(this.ItemMultiChoiceMode);
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        this.service = null;
        this.serviceState = null;
    }

    private void initTitle() {
        setTitle(res.getString(R.string.app_name));
    }

    private void updateTitle() {
        if (serviceState.hasParent()) {
            setTitle(serviceState.getComponentName());
        } else {
            initTitle();
        }

    }

    private void updateListView() {
        List<LibraryComponent> componentList = serviceState.getCurrentComponentList();
        adapter.setComponentList(componentList);
        adapter.notifyDataSetChanged();
    }

    private void initProgressBar() {
        int max = serviceState.getComponentMaxChildrenCount();
        progressBar.setMax(max);
        progressBar.setVisibility(View.VISIBLE);
        updateProgressBar();
    }

    private void updateProgressBar() {
        if (LoadingState.LOADED == serviceState.getCurrentLoadingState()) {
            progressBar.setVisibility(View.GONE);
        } else {
            int currentProgress = serviceState.getCurrentProgress();
            progressBar.setProgress(currentProgress);
        }
    }

    private void callSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void callTagFormActivity(List<LibraryComponent> components) {
        service.initMultiTagContextualState(components);
        Intent intent = new Intent(this, TagFormActivity.class);
//        SharedObject.provideComponents(components);
//        SharedObject.provideComponentFactory(componentFactory);
        startActivityForResult(intent, TAG_UPDATE_REQUEST);
    }

    public void callOrganisationActivity(LibraryComponent component) {
        List<LibraryComponent> components = new ArrayList<>();
        components.add(component);
        callOrganisationActivity(components);
    }

    public void callOrganisationActivity(List<LibraryComponent> components) {
        Intent intent = new Intent(this, OrganisationActivity.class);
//        SharedObject.provideComponents(components);
        startActivityForResult(intent, ORGANISATION_REQUEST);
    }

    /**
     * Switch the view to the specified node.
     * If the node has not been loaded yet, then it is loaded.
     *
     * @param composite The composite to switch to.
     */
    private void switchComposite(LibraryComposite composite) {
        serviceState.switchComposite(composite);
        serviceState.loadCurrentComposite(false, updateListViewCallback);
        updateActivityMetaData();
    }

    /**
     * Switch the current node to the parent node.
     *
     * @return True if the adapter has switch to the parent node.
     */
    public boolean backToParent() {
        boolean backToParent = serviceState.backToParentComposite();

        if (backToParent) {
            serviceState.loadCurrentComposite(false, updateListViewCallback);
            updateActivityMetaData();
        }

        return backToParent;
//        LibraryComposite parent = adapter.getComposite().getParent();
//        if (parent == null) {
//            return false;
//        } else {
//            switchComposite(parent);
//            return true;
//        }
    }

    public void updateActivityMetaData() {
        updateTitle();
        initProgressBar();
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
        }
    }
}
