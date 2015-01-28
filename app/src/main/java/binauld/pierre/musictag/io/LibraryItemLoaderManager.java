package binauld.pierre.musictag.io;


import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import binauld.pierre.musictag.collection.LibraryItemComparator;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.item.LibraryItem;

/**
 * A manager to handle all loader.
 * Enable to cancel loading when app is closed.
 */
public class LibraryItemLoaderManager {

    private Set<WeakReference<LibraryItemLoader>> loaders = new HashSet<>();

    //    private LibraryItemAdapter adapter;
    private LibraryItemFactory itemFactory;
    private int updateStep;

    public LibraryItemLoaderManager(LibraryItemFactory itemFactory, int updateStep) {
//        this.adapter = adapter;
        this.itemFactory = itemFactory;
        this.updateStep = updateStep;
    }

    /**
     * Cancel all loader running.
     *
     * @param mayInterruptIfRunning true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
     */
    public void cancelAll(boolean mayInterruptIfRunning) {
        for (WeakReference<LibraryItemLoader> ref : loaders) {
            LibraryItemLoader loader = ref.get();
            if (null != loader) {
                loader.cancel(mayInterruptIfRunning);
            }
        }
    }


    /**
     * Create and register a new loader.
     *
     * @return The created loader.
     */
    public LibraryItemLoader get(boolean drillDown, LibraryItemLoader.Callback callback) {
        Comparator<LibraryItem> comparator = new LibraryItemComparator();

        LibraryItemLoader loader = new LibraryItemLoader(itemFactory, comparator, updateStep, drillDown, callback);

        loaders.add(new WeakReference<>(loader));
        return loader;
    }

}
