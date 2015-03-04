package binauld.pierre.musictag.service.state.impl;

import java.util.List;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.service.ServiceWorker;
import binauld.pierre.musictag.service.action.FilenamesBuilderAction;
import binauld.pierre.musictag.service.action.MultipleTagAction;
import binauld.pierre.musictag.service.state.MultiTagContextualState;
import binauld.pierre.musictag.service.task.BreadthFirstTask;
import binauld.pierre.musictag.service.task.LoadingTaskBuilder;
import binauld.pierre.musictag.service.task.Task;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;

public class MultiTagContextualStateImpl implements MultiTagContextualState {

    private StringBuilder filenamesBuilder;
    private MultipleId3Tag multipleId3Tag;
    private List<LibraryComponent> components;

    private LoadingTaskBuilder loadingTaskBuilder;
    private ServiceWorker serviceWorker;

    public MultiTagContextualStateImpl(ServiceWorker serviceWorker, LoadingTaskBuilder loadingTaskBuilder, List<LibraryComponent> components) {
        this.components = components;
        this.serviceWorker = serviceWorker;
        this.loadingTaskBuilder = loadingTaskBuilder;
        this.filenamesBuilder = new StringBuilder();

    }

    @Override
    public MultipleId3Tag getMultiTag() {
        return multipleId3Tag;
    }

    @Override
    public String getFilenames() {
        return filenamesBuilder.toString();
    }

    @Override
    public void launchComponentsLoading() {
        LibraryComponentLoaderVisitor loaderVisitor = new LibraryComponentLoaderVisitor(null);

        for (LibraryComponent component : components) {
            component.accept(loaderVisitor);
        }

    }

    @Override
    public void launchMultiTagCreation(Runnable callback) {
        multipleId3Tag = new MultipleId3Tag();
        filenamesBuilder = new StringBuilder();

        for (LibraryComponent component : components) {
            BreadthFirstTask task = new BreadthFirstTask(component);

            FilenamesBuilderAction filenamesBuilderAction = new FilenamesBuilderAction(filenamesBuilder);
            task.addAction(filenamesBuilderAction);
            task.addOnPostExecuteCallbacks(filenamesBuilderAction);

            MultipleTagAction multipleTagAction = new MultipleTagAction(multipleId3Tag);
            task.addAction(multipleTagAction);

            task.addOnPostExecuteCallbacks(callback);
            serviceWorker.pushTask(task);
        }
    }

    @Override
    public void launchSaving(Runnable callback) {
        for (LibraryComponent component : components) {
//            serviceWorker.pushTask(taskBuilder.buildSavingTask(multipleId3Tag, component, callback));
        }
    }

    class LibraryComponentLoaderVisitor implements ComponentVisitor {

        private Runnable callback;

        public LibraryComponentLoaderVisitor(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void visit(LibraryLeaf leaf) {

        }

        @Override
        public void visit(LibraryComposite composite) {
            Task loadingTask = loadingTaskBuilder.build(composite, true, callback);
            serviceWorker.pushTask(loadingTask);
        }
    }
}
