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

//        List<LibraryComposite> items = new ArrayList<>();
//        for (LibraryComponent item : params) {
//            if (!item.isAudioItem()) {
//                items.add((LibraryComposite) item);
//            }
//        }
//
        for (LibraryComponent component : params) {
//            loadItemTree(item);
            LoadComponentVisitor loadComponentVisitor = new LoadComponentVisitor();
            component.accept(loadComponentVisitor);
        }

//        Result result = new Result();
//        result.foldersItems = items;
//        return result;
        return null;
    }

    @Override
    protected void onProgressUpdate(Progress... progresses) {
        for (Progress progress : progresses) {
            progress.composite.getChildren().pull();
//            progress.NodeItems.pull();
            progress.composite.setInvalidComponentCount(progress.invalidComponentCount);
//            progress.currentItem.setState(progress.state);
            callback.onProgressUpdate(progress.rootComposite);
        }
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void result) {
//        items.pull();
//        NodeItems.pull();
//        rootComposite.setState(LoadingState.LOADED);
        callback.onPostExecute();
//        adapter.notifyDataSetChanged();
    }


//    public void loadItemTree(LibraryComposite rootItem) {
//        Queue<LibraryComposite> queue = new LinkedList<>();
//        queue.add(rootItem);
//
//        do {
//            LibraryComposite currentNodeItem = queue.poll();
//
////            int invalidItemCount = 0;
//            if (currentNodeItem.getState() == LoadingState.LOADING) {
//                queue.add(currentNodeItem);
//            } else {
//                if (currentNodeItem.getState() == LoadingState.NOT_LOADED) {
//                    currentNodeItem.setState(LoadingState.LOADING);
//
//                    loadComposite(rootItem, currentNodeItem);
//                }
//
//                queue.addAll(currentNodeItem.getNodeItems());
//            }
//        } while (!queue.isEmpty() && drillDown);
//    }

//    private void loadComposite(LibraryComposite rootComposite, LibraryComposite currentNodeItem) {
//
//        int invalidItemCount = 0;
//
//        MultipleBufferedList<LibraryComponent> currentItems = currentNodeItem.getChildren();
//        List<LibraryComposite> currentNodeItems = currentNodeItem.getNodeItems();
//        File[] currentFiles = ((FolderImpl) currentNodeItem.getItem()).getFileList();
//
//        if (null != currentFiles) {
//
//            int j = 0;
//            for (int i = 0; i < currentFiles.length; i++) {
//
//                try {
//                    LibraryComponent item = factory.build(currentFiles[i], currentNodeItem);
//
//                    if (!item.isAudioItem()) {
//                        LibraryComposite NodeItem = (LibraryComposite) item;
//                        currentNodeItems.add(NodeItem);
//                    }
//                    currentItems.add(item);
//
//                } catch (IOException e) {
//                    invalidItemCount++;
//                    Log.w(this.getClass().toString(), e.getMessage());
//                }
//
//                j = ++j % updateStep;
//                if (j == 0 || i == currentFiles.length - 1) {
//                    Collections.sort(currentItems.getWorkingList(), comparator);
//                    currentItems.push();
////                    currentNodeItems.push();
//                    Progress progress = new Progress();
//                    progress.rootComposite = rootComposite;
//                    progress.currentItem = currentNodeItem;
//                    progress.childItems = currentItems;
//                    progress.NodeItems = currentNodeItems;
//                    progress.invalidItemCount = invalidItemCount;
//                    if (i == currentFiles.length - 1) {
//                        progress.state = LoadingState.LOADED;
//                    } else {
//                        progress.state = LoadingState.LOADING;
//                    }
//                    publishProgress(progress);
//                }
//            }
//        }
//    }

    class LoadComponentVisitor implements ComponentVisitor, ItemVisitor {

        private LibraryComposite rootComposite;
        private LibraryComposite currentComposite;
        private Queue<LibraryComponent> queue = new LinkedList<>();
        private CompositeFilterVisitor compositeFilterVisitor;

        LoadComponentVisitor() {
            this.compositeFilterVisitor = new CompositeFilterVisitor(queue);
        }

        @Override
        public void visit(LibraryLeaf leaf) {

        }

        @Override
        public void visit(LibraryComposite composite) {
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

                        component.accept(compositeFilterVisitor);
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

//        private void loadComposite(LibraryComposite composite) {
//
//            int invalidComponentCount = 0;
//
//            MultipleBufferedList<LibraryComponent> children = composite.getChildren();
////            List<LibraryComposite> currentNodeItems = composite.getNodeItems();
//            File[] currentFiles = ((Folder) composite.getItem()).getFileList();
//
//            if (null != currentFiles) {
//
//                int j = 0;
//                for (int i = 0; i < currentFiles.length; i++) {
//
//                    try {
//                        LibraryComponent component = factory.build(currentFiles[i], composite);
//
//                        component.accept(compositeFilterVisitor);
////                        if (!item.isAudioItem()) {
////                            LibraryComposite NodeItem = (LibraryComposite) item;
////                            currentNodeItems.add(NodeItem);
////                        }
//                        children.add(component);
//
//                    } catch (IOException e) {
//                        invalidComponentCount++;
//                        Log.w(this.getClass().toString(), e.getMessage());
//                    }
//
//                    if (i == currentFiles.length - 1) {
//                        composite.setState(LoadingState.LOADED);
//                    }
//
//                    j = ++j % updateStep;
//                    if (j == 0 || i == currentFiles.length - 1) {
//                        publishProgress(composite, invalidComponentCount);
////                        Collections.sort(currentItems.getWorkingList(), comparator);
////                        currentItems.push();
//////                    currentNodeItems.push();
////                        Progress progress = new Progress();
////                        progress.rootComposite = rootComposite;
////                        progress.composite = currentNodeItem;
////                        progress.children = currentItems;
//////                        progress.NodeItems = currentNodeItems;
////                        progress.invalidItemCount = invalidItemCount;
//////                        if (i == currentFiles.length - 1) {
//////                            progress.state = LoadingState.LOADED;
//////                        } else {
//////                            progress.state = LoadingState.LOADING;
//////                        }
////                        publishProgress(progress);
//                    }
//                }
//            }
//        }

        private void publishProgress(LibraryComposite composite, int invalidComponentCount) {
            Collections.sort(composite.getChildren().getWorkingList(), comparator);
            composite.getChildren().push();
//                    currentNodeItems.push();

            Progress progress = new Progress();
            progress.rootComposite = rootComposite;
            progress.composite = composite;
            progress.invalidComponentCount = invalidComponentCount;
//            progress.children = currentItems;
//                        progress.NodeItems = currentNodeItems;
//                        if (i == currentFiles.length - 1) {
//                            progress.state = LoadingState.LOADED;
//                        } else {
//                            progress.state = LoadingState.LOADING;
//                        }

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
        //        private LoadingState state;
        private LibraryComposite rootComposite;
        private LibraryComposite composite;
        //        private MultipleBufferedList<LibraryComponent> children;
//        private List<LibraryUComposite> NodeItems;
        private int invalidComponentCount;

    }

//    static class Result {
//        List<LibraryComposite> foldersItems;
//    }

    public static interface Callback {

        void onProgressUpdate(LibraryComposite composite);

        void onPostExecute(/*List<LibraryComposite> item*/);
    }
}
