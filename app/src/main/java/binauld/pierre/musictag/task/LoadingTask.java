package binauld.pierre.musictag.task;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import binauld.pierre.musictag.collection.MultipleBufferedList;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.composite.LoadingState;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class LoadingTask extends Task implements ComponentVisitor, ItemVisitor {

    private LibraryComposite rootComposite;
    private LibraryComposite currentComposite;
    private LibraryComponentFactory factory;

    private int updateStep;
    private boolean drillDown = false;

    private Queue<LibraryComponent> queue = new LinkedList<>();
    private CompositeFilterVisitor filterVisitor;
    private Comparator<LibraryComponent> comparator;


    public LoadingTask(LibraryComposite composite, LibraryComponentFactory factory, Comparator<LibraryComponent> comparator, int updateStep) {
        this.rootComposite = composite;
        this.factory = factory;
        this.comparator = comparator;
        this.updateStep = updateStep;

        this.filterVisitor = new CompositeFilterVisitor(this.queue);
        this.queue.add(rootComposite);
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            queue.poll().accept(this);
        }
    }


    @Override
    public void visit(LibraryLeaf leaf) {

    }

    @Override
    public void visit(LibraryComposite composite) {
        currentComposite = composite;
        if (currentComposite.getState() == LoadingState.NOT_LOADED) {
            currentComposite.setState(LoadingState.LOADING);
            currentComposite.getItem().accept(this);
            currentComposite.setState(LoadingState.LOADED);
            currentComposite.setFullyLoaded(drillDown);

        } else if (currentComposite.getState() == LoadingState.LOADING) {
            this.postpone(currentComposite);

        } else if (!currentComposite.isFullyLoaded() && drillDown) {
            for (LibraryComponent component : currentComposite.getChildren()) {
                this.postpone(component);
            }
        }
        publish();
    }

    private void postpone(LibraryComponent component) {
        queue.add(component);
    }

    @Override
    public void visit(AudioFile audioFile) {

    }

    @Override
    public void visit(Folder folder) {
        int invalidComponentCount = 0;

        MultipleBufferedList<LibraryComponent> children = currentComposite.getChildren();
        File[] currentFiles = folder.getFileList();

        if (null != currentFiles) {

            int j = 0;
            for (int i = 0; i < currentFiles.length; i++) {

                try {
                    LibraryComponent component = factory.build(currentFiles[i], currentComposite);

                    if (drillDown) {
                        component.accept(filterVisitor);
                    }
                    children.add(component);

                } catch (IOException e) {
                    invalidComponentCount++;
                    currentComposite.setInvalidComponentCount(invalidComponentCount);
                    Log.w(this.getClass().toString(), e.getMessage());
                }

                j = ++j % updateStep;
                if (j == 0 /*|| i == currentFiles.length - 1*/) {
                    publish();
                }
            }
        }
    }

    @Override
    protected void publish() {
        MultipleBufferedList<LibraryComponent> children = currentComposite.getChildren();
        Collections.sort(children.getWorkingList(), comparator);
        children.push();
        super.publish();
    }

    public void setDrillDown(boolean drillDown) {
        this.drillDown = drillDown;
    }

    class CompositeFilterVisitor implements ComponentVisitor {

        private Collection<LibraryComponent> collection;

        CompositeFilterVisitor(Collection<LibraryComponent> collection) {
            this.collection = collection;
        }

        @Override
        public void visit(LibraryLeaf leaf) {

        }

        @Override
        public void visit(LibraryComposite composite) {
            collection.add(composite);
        }
    }
}
