package binauld.pierre.musictag.service.task;

import android.content.res.Resources;

import java.util.Comparator;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.collection.LibraryItemComparator;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.factory.LibraryComponentFactory;

public class LoadingTaskBuilder {

    private LibraryComponentFactory componentFactory;
    private Comparator<LibraryComponent> comparator = new LibraryItemComparator();
    private int libraryLoaderUpdateStep;

    public LoadingTaskBuilder(Resources res, LibraryComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        libraryLoaderUpdateStep = res.getInteger(R.integer.library_loader_update_step);
    }

    public Task build(LibraryComposite composite, boolean drillDown, Runnable callback) {
        LoadingTask task = new LoadingTask(composite, componentFactory, comparator, libraryLoaderUpdateStep);
        task.setDrillDown(drillDown);
        task.addOnProgressUpdateCallback(callback);

        return task;
    }
}
