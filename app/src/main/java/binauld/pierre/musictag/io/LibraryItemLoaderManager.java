package binauld.pierre.musictag.io;


import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.helper.LoaderHelper;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * A manager to handle all loader.
 * Enable to cancel loading when app is closed.
 */
public class LibraryItemLoaderManager {

    private Set<WeakReference<LibraryItemLoader>> loaders = new HashSet<>();

    private LibraryItemAdapter adapter;
    private LibraryItemFactory itemFactory;
    private ProgressBar progressBar;

    public LibraryItemLoaderManager(LibraryItemAdapter adapter, LibraryItemFactory itemFactory, ProgressBar progressBar) {
        this.adapter = adapter;
        this.itemFactory = itemFactory;
        this.progressBar = progressBar;
    }

    /**
     * Create and register a new loader.
     * @return The created loader.
     */
    public LibraryItemLoader get() {
        LibraryItemLoader loader = LoaderHelper.buildLoader(adapter, itemFactory, this);
        loader.setProgressBar(progressBar);
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
}
