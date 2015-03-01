package binauld.pierre.musictag.service.state.impl;

import java.util.List;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LoadingState;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.service.state.LibraryServiceState;
import binauld.pierre.musictag.service.ServiceWorker;
import binauld.pierre.musictag.service.task.Task;
import binauld.pierre.musictag.service.task.TaskBuilder;
import binauld.pierre.musictag.visitor.impl.MaxChildrenCountExtractor;

public class LibraryServiceStateImpl implements LibraryServiceState {

    private LibraryComposite composite;
    private ServiceWorker worker;
    private TaskBuilder taskBuilder;

    public LibraryServiceStateImpl(LibraryComposite composite, ServiceWorker worker, TaskBuilder taskBuilder) {
        this.composite = composite;
        this.worker = worker;
        this.taskBuilder = taskBuilder;
    }

    @Override
    public void switchComposite(LibraryComposite composite) {
        this.composite = composite;
    }

    @Override
    public boolean backToParentComposite() {
        LibraryComposite parent = composite.getParent();
        if (null == parent) {
            return false;
        } else {
            composite = parent;
            return true;
        }
    }

    @Override
    public int getComponentMaxChildrenCount() {
        Item item = composite.getItem();

        MaxChildrenCountExtractor maxChildrenCountExtractor = new MaxChildrenCountExtractor();

        item.accept(maxChildrenCountExtractor);

        return maxChildrenCountExtractor.getListMaxSize();
    }

    @Override
    public int getCurrentProgress() {
        return composite.size() + composite.getInvalidComponentCount();
    }

    @Override
    public LoadingState getCurrentLoadingState() {
        return composite.getState();
    }

    @Override
    public boolean hasParent() {
        return composite.getParent() != null;
    }

    @Override
    public String getComponentName() {
        return composite.getItem().getPrimaryInformation();
    }

    @Override
    public List<LibraryComponent> getCurrentComponentList() {
        composite.getChildren().pull();
        return composite.getChildren();
    }

    @Override
    public void loadCurrentComposite(boolean drillDown, Runnable callback) {
        asyncLoad(composite, drillDown, callback);
    }

    public void load(LibraryComposite composite, boolean drillDown, Runnable callback) {
        if (this.composite.getState() == LoadingState.NOT_LOADED || drillDown) {
            Task task = taskBuilder.buildLoadingTask(composite, drillDown, callback);

            worker.pushTask(task);
        } else {
            callback.run();
        }
    }

    public void asyncLoad(LibraryComposite composite, boolean drillDown, Runnable callback) {
        if (this.composite.getState() == LoadingState.NOT_LOADED || drillDown) {
            Task task = taskBuilder.buildLoadingTask(composite, drillDown, callback);

            worker.launchAsyncTask(task);
        } else {
            callback.run();
        }
    }
}
