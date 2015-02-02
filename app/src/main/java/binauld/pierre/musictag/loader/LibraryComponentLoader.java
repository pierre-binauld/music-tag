package binauld.pierre.musictag.loader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import binauld.pierre.musictag.collection.MultipleBufferedList;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LoadingState;
import binauld.pierre.musictag.visitor.ItemVisitor;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryComponentLoader extends AsyncTask<LibraryComponent, LibraryComponentLoader.Progress, Void> {

    private int updateStep;
    private boolean drillDown;

    //    private MultipleBufferedList<LibraryItem> items;
//    private MultipleBufferedList<NodeItem> NodeItems;
    private LibraryComponentFactory factory;
//    private int invalidItemCount;

    //    private LibraryItemAdapter adapter;
//    private NodeItem rootComposite;
    private Comparator<LibraryComponent> comparator;


    private Callback callback;


    public LibraryComponentLoader(LibraryComponentFactory libraryComponentFactory, Comparator<LibraryComponent> comparator, int updateStep, boolean drillDown, Callback callback) {
        this.updateStep = updateStep;
        this.drillDown = drillDown;

        this.callback = callback;

//        this.adapter = adapter;
//        this.rootComposite = node;
        this.factory = libraryComponentFactory;

        this.comparator = comparator;
    }

    @Override
    protected Void doInBackground(LibraryComponent... params) {
        for (LibraryComponent component : params) {
            LoadComponentVisitor loadComponentVisitor = new LoadComponentVisitor();
            loadComponentVisitor.load(component);
//            component.accept(loadComponentVisitor);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Progress... progresses) {
        for (Progress progress : progresses) {
            progress.composite.getChildren().pull();
            progress.composite.setInvalidComponentCount(progress.invalidComponentCount);
            callback.onProgressUpdate(progress.rootComposite);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        callback.onPostExecute();
    }

    class LoadComponentVisitor implements ComponentVisitor, ItemVisitor {

        private LibraryComposite rootComposite;
        private LibraryComposite currentComposite;
        private Queue<LibraryComponent> queue = new LinkedList<>();
        private CompositeFilterVisitor compositeFilterVisitor;

        LoadComponentVisitor() {
            this.compositeFilterVisitor = new CompositeFilterVisitor(queue);
        }

        public void load(LibraryComponent component) {
            queue.add(component);

            while (!queue.isEmpty()) {
                component = queue.poll();
                component.accept(this);
            }
        }

        @Override
        public void visit(LibraryLeaf leaf) {
        }

        @Override
        public void visit(LibraryComposite composite) {
            //TODO find another way to do this !
            if (null == rootComposite) {
                rootComposite = composite;
            }

            if (composite.getState() == LoadingState.LOADING) {
                queue.add(composite);
            } else {
                if (composite.getState() == LoadingState.NOT_LOADED) {
                    composite.setState(LoadingState.LOADING);
                    currentComposite = composite;
                    composite.getItem().accept(this);
//                    loadComposite(composite);
                }

//                queue.addAll(currentNodeItem.getNodeItems());
            }
        }

        @Override
        public void visit(AudioFile audioFile) {

        }

        @Override
        public void visit(Folder folder) {
            int invalidComponentCount = 0;

            MultipleBufferedList<LibraryComponent> children = currentComposite.getChildren();
            File[] currentFiles = ((Folder) currentComposite.getItem()).getFileList();

            if (null != currentFiles) {

                int j = 0;
                for (int i = 0; i < currentFiles.length; i++) {

                    try {
                        LibraryComponent component = factory.build(currentFiles[i], currentComposite);

                        if(drillDown) {
                            component.accept(compositeFilterVisitor);
                        }
                        children.add(component);

                    } catch (IOException e) {
                        invalidComponentCount++;
                        Log.w(this.getClass().toString(), e.getMessage());
                    }

                    if (i == currentFiles.length - 1) {
                        currentComposite.setState(LoadingState.LOADED);
                    }

                    j = ++j % updateStep;
                    if (j == 0 || i == currentFiles.length - 1) {
                        publishProgress(currentComposite, invalidComponentCount);
                    }
                }
            }
        }


        private void publishProgress(LibraryComposite composite, int invalidComponentCount) {
            Collections.sort(composite.getChildren().getWorkingList(), comparator);
            composite.getChildren().push();

            Progress progress = new Progress();
            progress.rootComposite = rootComposite;
            progress.composite = composite;
            progress.invalidComponentCount = invalidComponentCount;

            LibraryComponentLoader.this.publishProgress(progress);
        }
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

    static class Progress {
        private LibraryComposite rootComposite;
        private LibraryComposite composite;
        private int invalidComponentCount;
    }

    public static interface Callback {

        void onProgressUpdate(LibraryComposite composite);

        void onPostExecute();
    }
}
