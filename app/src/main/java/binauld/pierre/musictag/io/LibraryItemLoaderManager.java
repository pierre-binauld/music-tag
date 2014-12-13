package binauld.pierre.musictag.io;


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

    Set<LibraryItemLoader> loaders = new HashSet<LibraryItemLoader>();
    private LibraryItemAdapter adapter;
    private ThumbnailService thumbnailService;

    public LibraryItemLoaderManager(LibraryItemAdapter adapter, ThumbnailService thumbnailService) {
        this.adapter = adapter;
        this.thumbnailService = thumbnailService;
    }

    /**
     * Create and register a new loader.
     * @return The created loader.
     */
    public LibraryItemLoader get() {
        LibraryItemLoader loader = LoaderHelper.buildLoader(adapter, thumbnailService, this);
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

    public void remove(LibraryItemLoader loader) {
        loaders.remove(loader);
    }
}
