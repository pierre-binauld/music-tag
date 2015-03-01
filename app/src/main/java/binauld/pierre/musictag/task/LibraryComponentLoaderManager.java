//package binauld.pierre.musictag.task;
//
//
//import java.lang.ref.WeakReference;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.Set;
//
//import binauld.pierre.musictag.collection.LibraryItemComparator;
//import binauld.pierre.musictag.factory.LibraryComponentFactory;
//import binauld.pierre.musictag.composite.LibraryComponent;
//
///**
// * A manager to handle all loader.
// * Enable to cancel loading when app is closed.
// */
//public class LibraryComponentLoaderManager {
//
//    private Set<WeakReference<LibraryComponentLoader>> loaders = new HashSet<>();
//
//    private LibraryComponentFactory itemFactory;
//    private int updateStep;
//
//    public LibraryComponentLoaderManager(LibraryComponentFactory itemFactory, int updateStep) {
//        this.itemFactory = itemFactory;
//        this.updateStep = updateStep;
//    }
//
//    /**
//     * Cancel all loader running.
//     *
//     * @param mayInterruptIfRunning true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
//     */
//    public void cancelAll(boolean mayInterruptIfRunning) {
//        for (WeakReference<LibraryComponentLoader> ref : loaders) {
//            LibraryComponentLoader loader = ref.get();
//            if (null != loader) {
//                loader.cancel(mayInterruptIfRunning);
//            }
//        }
//    }
//
//
//    /**
//     * Create and register a new loader.
//     *
//     * @return The created loader.
//     */
//    public LibraryComponentLoader get(boolean drillDown, LibraryComponentLoader.Callback callback) {
//        Comparator<LibraryComponent> comparator = new LibraryItemComparator();
//
//        LibraryComponentLoader loader = new LibraryComponentLoader(itemFactory, comparator, updateStep, drillDown, callback);
//
//        loaders.add(new WeakReference<>(loader));
//        return loader;
//    }
//
//}
