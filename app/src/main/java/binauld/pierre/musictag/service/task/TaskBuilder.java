package binauld.pierre.musictag.service.task;

import android.content.res.Resources;

import java.util.Comparator;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.collection.LibraryItemComparator;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.tag.MultipleId3Tag;

public class TaskBuilder {

    private LibraryComponentFactory componentFactory;
    private Comparator<LibraryComponent> comparator = new LibraryItemComparator();
    private int libraryLoaderUpdateStep;

    public TaskBuilder(Resources res, LibraryComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        libraryLoaderUpdateStep = res.getInteger(R.integer.library_loader_update_step);
    }

    public Task buildLoadingTask(LibraryComposite composite, boolean drillDown, Runnable callback) {
        LoadingTask task = new LoadingTask(composite, componentFactory, comparator, libraryLoaderUpdateStep);
        task.setDrillDown(drillDown);
        task.addOnProgressUpdateCallbacks(callback);

        return task;
    }

    public Task buildMultiTagTask(MultipleId3Tag multiId3Tag, LibraryComponent component, Runnable callback) {
        MultiTagTask multiTagTask = new MultiTagTask(multiId3Tag, component);
        multiTagTask.addOnPostExecuteCallbacks(callback);
        return multiTagTask;
    }

    public Task buildSavingTask(MultipleId3Tag multipleId3Tag, LibraryComponent component, Runnable callback) {
        SavingTask savingTask = new SavingTask(multipleId3Tag, component);
        savingTask.addOnPostExecuteCallbacks(callback);
        return savingTask;
    }
}
