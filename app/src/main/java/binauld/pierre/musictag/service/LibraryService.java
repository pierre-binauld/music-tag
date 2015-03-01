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

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.helper.LibraryComponentFactoryHelper;
import binauld.pierre.musictag.service.state.LibraryServiceState;
import binauld.pierre.musictag.service.state.impl.LibraryServiceStateImpl;
import binauld.pierre.musictag.task.AsyncTaskExecutor;
import binauld.pierre.musictag.service.task.TaskBuilder;
import binauld.pierre.musictag.wrapper.FileWrapper;
import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;

public class LibraryService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final IBinder binder = new LibraryServiceBinder();

    private Resources res;
    private SharedPreferences sharedPrefs;

    private ServiceWorker worker = new ServiceWorker();

    private LibraryComponentFactory componentFactory;

    private LibraryServiceState serviceState;


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Library Service created.", Toast.LENGTH_SHORT).show();

        // Init worker
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

        // Init TaskBuilder
        TaskBuilder taskBuilder = new TaskBuilder(res, componentFactory);


        // Init state
        serviceState = new LibraryServiceStateImpl(getComposite(), worker, taskBuilder);

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(res.getString(R.string.source_folder_preference_key))) {
            serviceState.switchComposite(getComposite());
        }
    }

    public LibraryComposite getComposite() {
        String sourceFolder = sharedPrefs.getString(
                res.getString(R.string.source_folder_preference_key),
                res.getString(R.string.source_folder_preference_default));

        return componentFactory.buildComposite(new File(sourceFolder), null);
    }

    public class LibraryServiceBinder extends Binder {
        public LibraryServiceState getServiceWrapper() {
            return LibraryService.this.serviceState;
        }

    }
}
