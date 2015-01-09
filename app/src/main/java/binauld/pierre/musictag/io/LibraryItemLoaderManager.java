package binauld.pierre.musictag.io;


import android.os.AsyncTask;
import android.os.Build;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.helper.LoaderHelper;

/**
 * A manager to handle all loader.
 * Enable to cancel loading when app is closed.
 */
public class LibraryItemLoaderManager {

    private Set<WeakReference<LibraryItemLoader>> loaders = new HashSet<>();

    private LibraryItemAdapter adapter;
    private LibraryItemFactory itemFactory;
    private int updateStep;

    public LibraryItemLoaderManager(LibraryItemAdapter adapter, LibraryItemFactory itemFactory, int updateStep) {
        this.adapter = adapter;
        this.itemFactory = itemFactory;
        this.updateStep = updateStep;
    }

    /**
     * Create and register a new loader.
     * @return The created loader.
     */
    public LibraryItemLoader get() {
        LibraryItemLoader loader = LoaderHelper.buildLoader(adapter, itemFactory, this, updateStep);
        loaders.add(new WeakReference<>(loader));
        return loader;
    }

    /**
     * Cancel all loader running.
     * @param mayInterruptIfRunning true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
     */
    public void cancelAll(boolean mayInterruptIfRunning) {
        for (WeakReference<LibraryItemLoader> ref : loaders) {
            LibraryItemLoader loader = ref.get();
            if(null != loader) {
                loader.cancel(mayInterruptIfRunning);
            }
        }
    }

    /**
     * Execute a loader for specified files.
     * @param loader The loader to execute.
     * @param files Files to load.
     */
    public void execute(LibraryItemLoader loader, File[] files) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, files);
        } else {
            loader.execute(files);
        }
    }
}
