package binauld.pierre.musictag.service.state.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.service.ServiceWorker;
import binauld.pierre.musictag.service.action.FilenamesBuilderAction;
import binauld.pierre.musictag.service.action.Id3TagUpdaterAction;
import binauld.pierre.musictag.service.action.ModifiedId3TagCreatorAction;
import binauld.pierre.musictag.service.action.MultipleTagUpdaterAction;
import binauld.pierre.musictag.service.action.SavingAction;
import binauld.pierre.musictag.service.state.MultipleTagContextualState;
import binauld.pierre.musictag.service.task.BreadthFirstTask;
import binauld.pierre.musictag.service.task.CallbackTask;
import binauld.pierre.musictag.service.task.LoadingTaskBuilder;
import binauld.pierre.musictag.service.task.ModifiedId3TagsTask;
import binauld.pierre.musictag.service.task.Task;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.wrapper.FileWrapper;

public class MultipleTagContextualStateImpl implements MultipleTagContextualState {

    private StringBuilder filenamesBuilder;
    private MultipleId3Tag multipleId3Tag;
    private List<LibraryComponent> components;
    private Map<AudioFile, Id3Tag> modifiedId3Tags;

    private LoadingTaskBuilder loadingTaskBuilder;
    private ServiceWorker serviceWorker;
    private FileWrapper fileWrapper;

    public MultipleTagContextualStateImpl(ServiceWorker serviceWorker, LoadingTaskBuilder loadingTaskBuilder, List<LibraryComponent> components, FileWrapper fileWrapper) {
        this.components = components;
        this.serviceWorker = serviceWorker;
        this.loadingTaskBuilder = loadingTaskBuilder;
        this.fileWrapper = fileWrapper;
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
    public Integer getItemCount() {
        if (null != modifiedId3Tags) {
            return modifiedId3Tags.size();
        } else {
            return -1;
        }
    }

    @Override
    public Map<AudioFile, Id3Tag> getModifiedId3Tags() {
        return modifiedId3Tags;
    }

    @Override
    public void launchComponentsLoading() {
        LibraryComponentLoaderVisitor loaderVisitor = new LibraryComponentLoaderVisitor(null);

        for (LibraryComponent component : components) {
            component.accept(loaderVisitor);
        }

    }

    @Override
    public void launchMultiTagCreation(List<Runnable> callbacks) {
        multipleId3Tag = new MultipleId3Tag();
        filenamesBuilder = new StringBuilder();
        modifiedId3Tags = new HashMap<>();

        for (LibraryComponent component : components) {
            BreadthFirstTask task = new BreadthFirstTask(component);

            ModifiedId3TagCreatorAction modifiedId3TagCreatorAction = new ModifiedId3TagCreatorAction(modifiedId3Tags);
            task.addAction(modifiedId3TagCreatorAction);

            FilenamesBuilderAction filenamesBuilderAction = new FilenamesBuilderAction(filenamesBuilder);
            task.addAction(filenamesBuilderAction);
            task.addOnPostExecuteCallback(filenamesBuilderAction);

            MultipleTagUpdaterAction multipleTagUpdaterAction = new MultipleTagUpdaterAction(multipleId3Tag);
            task.addAction(multipleTagUpdaterAction);


            serviceWorker.pushTask(task);
        }

        CallbackTask callbackTask = new CallbackTask();
        for (Runnable callback : callbacks) {
            callbackTask.addOnPostExecuteCallback(callback);
        }
        serviceWorker.pushTask(callbackTask);
    }

    @Override
    public void launchSaving(MultipleId3Tag multipleId3Tag, List<Runnable> callbacks) {
        this.multipleId3Tag = multipleId3Tag;
        Id3TagUpdaterAction id3TagUpdaterAction = new Id3TagUpdaterAction(multipleId3Tag);
        SavingAction savingAction = new SavingAction(fileWrapper);

        ModifiedId3TagsTask task = new ModifiedId3TagsTask(modifiedId3Tags);
        task.addAction(id3TagUpdaterAction);
        task.addAction(savingAction);

        for (Runnable callback : callbacks) {
            task.addOnPostExecuteCallback(callback);
        }
        serviceWorker.pushTask(task);
    }

    @Override
    public void launchUpdateModifiedId3Tag(MultipleId3Tag multipleId3Tag, List<Runnable> callbacks) {
        this.multipleId3Tag = multipleId3Tag;
        Id3TagUpdaterAction id3TagUpdaterAction = new Id3TagUpdaterAction(multipleId3Tag);

        ModifiedId3TagsTask task = new ModifiedId3TagsTask(modifiedId3Tags);
        task.addAction(id3TagUpdaterAction);

        for (Runnable callback : callbacks) {
            task.addOnPostExecuteCallback(callback);
        }
        serviceWorker.pushTask(task);
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
