package binauld.pierre.musictag.io;


import android.content.res.Resources;
import android.widget.ProgressBar;

import java.util.HashSet;
import java.util.Set;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.helper.LoaderHelper;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * A manager to handle all loader.
 * Enable to cancel loading when app is closed.
 */
public class LibraryItemLoaderManager {

    private Set<LibraryItemLoader> loaders = new HashSet<LibraryItemLoader>();

    private LibraryItemAdapter adapter;
    private ThumbnailService thumbnailService;
    private ProgressBar progressBar;

    public LibraryItemLoaderManager(LibraryItemAdapter adapter, ThumbnailService thumbnailService, ProgressBar progressBar) {
        this.adapter = adapter;
        this.thumbnailService = thumbnailService;
        this.progressBar = progressBar;
    }

    /**
     * Create and register a new loader.
     * @return The created loader.
     */
    public LibraryItemLoader get() {
        LibraryItemLoader loader = LoaderHelper.buildLoader(adapter, thumbnailService, this);
        loader.setProgressBar(progressBar);
        loaders.add(loader);
        return loader;
    }

    /**
     * Cancel all loader running.
     * @param mayInterruptIfRunning true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
     */
    public void cancelAll(boolean mayInterruptIfRunning) {
        for (LibraryItemLoader loader : loaders) {
            loader.cancel(mayInterruptIfRunning);
        }
    }

    /**
     * Remove a loader from the manager.
     * @param loader The loader to remove.
     */
    public void remove(LibraryItemLoader loader) {
        loaders.remove(loader);
    }
}
