package binauld.pierre.musictag.service.state.impl;

import android.util.Log;

import java.util.List;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.service.ServiceWorker;
import binauld.pierre.musictag.service.state.MultiTagContextualState;
import binauld.pierre.musictag.service.task.LoadingTask;
import binauld.pierre.musictag.service.task.Task;
import binauld.pierre.musictag.service.task.TaskBuilder;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.impl.LibraryComponentLoaderVisitor;

public class MultiTagContextualStateImpl implements MultiTagContextualState {

    private MultipleId3Tag multipleId3Tag;
    private List<LibraryComponent> components;

    private TaskBuilder taskBuilder;
    private ServiceWorker serviceWorker;

    public MultiTagContextualStateImpl(ServiceWorker serviceWorker, TaskBuilder taskBuilder, List<LibraryComponent> components) {
        this.components = components;
        this.serviceWorker = serviceWorker;
        this.taskBuilder = taskBuilder;
    }

    @Override
    public MultipleId3Tag getMultiTag() {
        return multipleId3Tag;
    }

    @Override
    public void launchComponentsLoading() {
        LibraryComponentLoaderVisitor loaderVisitor = new LibraryComponentLoaderVisitor(taskBuilder, serviceWorker, null);

        for (LibraryComponent component : components) {
            component.accept(loaderVisitor);
        }

    }

    @Override
    public void launchMultiTagCreation(Runnable callback) {
        multipleId3Tag = new MultipleId3Tag();
        for (LibraryComponent component : components) {
            serviceWorker.pushTask(taskBuilder.buildMultiTagTask(multipleId3Tag, component, callback));
        }
    }

    @Override
    public void launchSaving(Runnable callback) {
        for (LibraryComponent component : components) {
            serviceWorker.pushTask(taskBuilder.buildSavingTask(multipleId3Tag, component, callback));
        }
    }


}
