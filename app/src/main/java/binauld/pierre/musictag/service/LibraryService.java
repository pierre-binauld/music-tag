package binauld.pierre.musictag.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.activities.MainActivity;
import binauld.pierre.musictag.collection.LibraryItemComparator;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LoadingState;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.helper.LibraryComponentFactoryHelper;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.task.AsyncServiceWorker;
import binauld.pierre.musictag.task.AsyncTaskExecutor;
import binauld.pierre.musictag.task.LoadingTask;
import binauld.pierre.musictag.task.ServiceWorker;
import binauld.pierre.musictag.visitor.impl.MaxChildrenCountExtractor;
import binauld.pierre.musictag.wrapper.FileWrapper;
import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;

public class LibraryService extends Service implements MainActivity.LibraryServiceInterface, SharedPreferences.OnSharedPreferenceChangeListener {

    private final IBinder binder = new LibraryServiceBinder();
    private ServiceWorker worker = new ServiceWorker();

    private Resources res;
    private SharedPreferences sharedPrefs;

    private LibraryComposite composite;
    private LibraryComponentFactory componentFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Library Service created.", Toast.LENGTH_SHORT).show();
        AsyncTaskExecutor.execute(worker);

        // Init preference(s)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        // Init resources
        res = getResources();

        // Init default decoder
        BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);

        // Init Wrapper
        FileWrapper wrapper = new JAudioTaggerWrapper();

        // Init factory
        componentFactory = LibraryComponentFactoryHelper.buildFactory(res, wrapper, defaultArtworkBitmapDecoder);

        // Init composite
        reset();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Library Service destroyed.", Toast.LENGTH_SHORT).show();
        worker.cancel(true);
    }

    @Override
    public void switchComposite(LibraryComposite composite) {
        this.composite = composite;
    }

    @Override
    public boolean backToParentComposite() {
        LibraryComposite parent = composite.getParent();
        if (null == parent) {
            return false;
        } else {
            composite = parent;
            return true;
        }
    }

    @Override
    public int getComponentMaxChildrenCount() {
        Item item = composite.getItem();

        MaxChildrenCountExtractor maxChildrenCountExtractor = new MaxChildrenCountExtractor();

        item.accept(maxChildrenCountExtractor);

        return maxChildrenCountExtractor.getListMaxSize();
    }

    @Override
    public int getCurrentProgress() {
        return composite.size() + composite.getInvalidComponentCount();
    }

    @Override
    public LoadingState getCurrentState() {
        return composite.getState();
    }

    @Override
    public boolean hasParent() {
        return composite.getParent() != null;
    }

    @Override
    public String getComponentName() {
        return composite.getItem().getPrimaryInformation();
    }

    @Override
    public List<LibraryComponent> getCurrentComponentList() {
        composite.getChildren().pull();
        return composite.getChildren();
    }

    @Override
    public void loadCurrentComposite(boolean drillDown, Runnable callback) {
        asyncLoad(composite, drillDown, callback);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(res.getString(R.string.source_folder_preference_key))) {
            reset();
        }
    }

    public void reset() {
        composite = null;

        String sourceFolder = sharedPrefs.getString(
                res.getString(R.string.source_folder_preference_key),
                res.getString(R.string.source_folder_preference_default));

        LibraryComposite composite = componentFactory.buildComposite(new File(sourceFolder), null);
        switchComposite(composite);
    }

    public void load(LibraryComposite composite, boolean drillDown, Runnable callback) {
        if (this.composite.getState() == LoadingState.NOT_LOADED || drillDown) {
            int libraryLoaderUpdateStep = res.getInteger(R.integer.library_loader_update_step);
            Comparator<LibraryComponent> comparator = new LibraryItemComparator();

            LoadingTask task = new LoadingTask(composite, componentFactory, comparator, libraryLoaderUpdateStep);
            task.setDrillDown(drillDown);
            task.setCallback(callback);

            worker.addTask(task);
        } else {
            callback.run();
        }
    }

    public void asyncLoad(LibraryComposite composite, boolean drillDown, Runnable callback) {
        if (this.composite.getState() == LoadingState.NOT_LOADED || drillDown) {
            int libraryLoaderUpdateStep = res.getInteger(R.integer.library_loader_update_step);
            Comparator<LibraryComponent> comparator = new LibraryItemComparator();

            LoadingTask task = new LoadingTask(composite, componentFactory, comparator, libraryLoaderUpdateStep);
            task.setDrillDown(drillDown);
            task.setCallback(callback);

            AsyncServiceWorker worker = new AsyncServiceWorker(task);
            AsyncTaskExecutor.execute(worker);
        } else {
            callback.run();
        }
    }

    public class LibraryServiceBinder extends Binder {
        public MainActivity.LibraryServiceInterface getMainActivityServiceInterface() {
            return LibraryService.this;
        }

    }
}
