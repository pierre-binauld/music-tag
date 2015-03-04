package binauld.pierre.musictag.visitor.impl;

import android.util.Log;

import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.service.ServiceWorker;
import binauld.pierre.musictag.service.task.Task;
import binauld.pierre.musictag.service.task.TaskBuilder;
import binauld.pierre.musictag.visitor.ComponentVisitor;

public class LibraryComponentLoaderVisitor implements ComponentVisitor {

    private TaskBuilder taskBuilder;
    private ServiceWorker serviceWorker;
    private Runnable callback;

    public LibraryComponentLoaderVisitor(TaskBuilder taskBuilder, ServiceWorker serviceWorker, Runnable callback) {
        this.taskBuilder = taskBuilder;
        this.serviceWorker = serviceWorker;
        this.callback = callback;
    }

    @Override
    public void visit(LibraryLeaf leaf) {

    }

    @Override
    public void visit(LibraryComposite composite) {
        Task loadingTask = taskBuilder.buildLoadingTask(composite, true, callback);
        serviceWorker.pushTask(loadingTask);
    }
}
