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

//    public Set<FolderItem> loadingInProgress = new HashSet<>();
//    public Map<FolderItem, List<LibraryItemLoader.Callback>> callbacks = new HashMap<>();

    public LibraryItemLoaderManager(/*LibraryItemAdapter adapter,*/ LibraryItemFactory itemFactory, int updateStep) {
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


//    private List<LibraryItemLoader.Callback> getCallbacks(FolderItem item) {
//        List<LibraryItemLoader.Callback> callbackList = callbacks.get(item);
//        if (null == callbackList) {
//            callbackList = new ArrayList<>();
//            callbacks.put(item, callbackList);
//        }
//        return callbackList;
//    }

//    public void load(boolean drillDown, LibraryItemLoader.Callback callback, FolderItem... items) {
//        if (null != callback) {
//            for(FolderItem item : items) {
//                getCallbacks(item).add(callback);
//            }
//        }
//        load(items, drillDown, new ArrayList<LibraryItemLoader.Callback>());
//    }

//    public void load(boolean drillDown, List<LibraryItemLoader.Callback> callbackList, FolderItem... items) {
//
//        if (null != callbackList) {
//            getCallbacks(item).addAll(callbackList);
//        }
//
//        switch (item.getState()) {
//            case NOT_LOADED:
//                onNotLoaded(item, drillDown);
//                break;
//            case LOADING:
//                onLoading(item, drillDown);
//                break;
//            case LOADED:
//                onLoaded(item, drillDown);
//                break;
//        }
//    }

//    private void onNotLoaded(FolderItem item, boolean drillDown) {
//        LibraryItemLoader loader = get(drillDown);
//        AsyncTaskExecutor.execute(loader, item);
//    }
//
//    private void onLoading(FolderItem item, boolean drillDown) {
//        if (drillDown) {
//            loadingInProgress.add(item);
//        }
//    }
//
//    private void onLoaded(FolderItem item, boolean drillDown) {
//        onNotLoaded(item, drillDown);
//    }


    /**
     * Create and register a new loader.
     *
     * @return The created loader.
     */
    public LibraryItemLoader get(boolean drillDown, LibraryItemLoader.Callback callback) {
        Comparator<LibraryItem> comparator = new LibraryItemComparator();

//        LibraryItemLoader.Callback managerCallback = buildManagerCallback(drillDown);

        LibraryItemLoader loader = new LibraryItemLoader(itemFactory, comparator, updateStep, drillDown, callback);

        loaders.add(new WeakReference<>(loader));
        return loader;
    }

//    private LibraryItemLoader.Callback buildManagerCallback(final boolean drillDown) {
//        return new LibraryItemLoader.Callback() {
//
//            @Override
//            public void onProgressUpdate(FolderItem item) {
//                for (LibraryItemLoader.Callback callback : getCallbacks(item)) {
//                    callback.onProgressUpdate(item);
//                }
//            }
//
//            @Override
//            public void onPostExecute(FolderItem item) {
//                for (LibraryItemLoader.Callback callback : getCallbacks(item)) {
//                    callback.onPostExecute(item);
//                }
//                callbacks.remove(item);
//                if (loadingInProgress.contains(item) || drillDown) {
//                    onLoaded(item, true);
//                }
//            }
//        };
//    }

}
