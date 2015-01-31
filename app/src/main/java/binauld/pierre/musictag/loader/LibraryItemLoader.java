package binauld.pierre.musictag.loader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import binauld.pierre.musictag.collection.MultipleBufferedList;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.LoadingState;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.item.itemable.Folder;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryItemLoader extends AsyncTask<LibraryItem, LibraryItemLoader.Progress, LibraryItemLoader.Result> {

    private int updateStep;
    private boolean drillDown;

    //    private MultipleBufferedList<LibraryItem> items;
//    private MultipleBufferedList<NodeItem> NodeItems;
    private LibraryItemFactory factory;
//    private int invalidItemCount;

    //    private LibraryItemAdapter adapter;
//    private NodeItem rootItem;
    private Comparator<LibraryItem> comparator;

    private Callback callback;


    public LibraryItemLoader(LibraryItemFactory libraryItemFactory, Comparator<LibraryItem> comparator, int updateStep, boolean drillDown, Callback callback) {
        this.updateStep = updateStep;
        this.drillDown = drillDown;

        this.callback = callback;

//        this.adapter = adapter;
//        this.rootItem = node;
        this.factory = libraryItemFactory;

        this.comparator = comparator;
    }

    @Override
    protected LibraryItemLoader.Result doInBackground(LibraryItem... params) {

        List<NodeItem> items = new ArrayList<>();
        for (LibraryItem item : params) {
            if (!item.isAudioItem()) {
                items.add((NodeItem) item);
            }
        }

        for (NodeItem item : items) {
            loadItemTree(item);
        }

        Result result = new Result();
        result.foldersItems = items;
        return result;
    }

    @Override
    protected void onProgressUpdate(Progress... progresses) {
        for (Progress progress : progresses) {
            progress.childItems.pull();
//            progress.NodeItems.pull();
            progress.currentItem.setInvalidItemCount(progress.invalidItemCount);
            progress.currentItem.setState(progress.state);
            callback.onProgressUpdate(progress.rootItem);
        }
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Result result) {
//        items.pull();
//        NodeItems.pull();
//        rootItem.setState(LoadingState.LOADED);
        callback.onPostExecute(result.foldersItems);
//        adapter.notifyDataSetChanged();
    }

    public void loadItemTree(NodeItem rootItem) {
        Queue<NodeItem> queue = new LinkedList<>();
        queue.add(rootItem);

        do {
            NodeItem currentNodeItem = queue.poll();

//            int invalidItemCount = 0;
            if (currentNodeItem.getState() == LoadingState.LOADING) {
                queue.add(currentNodeItem);
            } else {
                if (currentNodeItem.getState() == LoadingState.NOT_LOADED) {
                    currentNodeItem.setState(LoadingState.LOADING);

                    loadItem(rootItem, currentNodeItem);
                }

                queue.addAll(currentNodeItem.getNodeItems());
            }
        } while (!queue.isEmpty() && drillDown);
    }

    private void loadItem(NodeItem rootItem, NodeItem currentNodeItem) {

        int invalidItemCount = 0;

        MultipleBufferedList<LibraryItem> currentItems = currentNodeItem.getChildren();
        List<NodeItem> currentNodeItems = currentNodeItem.getNodeItems();
        File[] currentFiles = ((Folder)currentNodeItem.getItemable()).getFileList();

        if (null != currentFiles) {

            int j = 0;
            for (int i = 0; i < currentFiles.length; i++) {

                try {
                    LibraryItem item = factory.build(currentFiles[i], currentNodeItem);

                    if (!item.isAudioItem()) {
                        NodeItem NodeItem = (NodeItem) item;
                        currentNodeItems.add(NodeItem);
                    }
                    currentItems.add(item);

                } catch (IOException e) {
                    invalidItemCount++;
                    Log.w(this.getClass().toString(), e.getMessage());
                }

                j = ++j % updateStep;
                if (j == 0 || i == currentFiles.length - 1) {
                    Collections.sort(currentItems.getWorkingList(), comparator);
                    currentItems.push();
//                    currentNodeItems.push();
                    Progress progress = new Progress();
                    progress.rootItem = rootItem;
                    progress.currentItem = currentNodeItem;
                    progress.childItems = currentItems;
                    progress.NodeItems = currentNodeItems;
                    progress.invalidItemCount = invalidItemCount;
                    if (i == currentFiles.length - 1) {
                        progress.state = LoadingState.LOADED;
                    } else {
                        progress.state = LoadingState.LOADING;
                    }
                    publishProgress(progress);
                }
            }
        }
    }

    static class Progress {
        private LoadingState state;
        private NodeItem rootItem;
        private NodeItem currentItem;
        private MultipleBufferedList<LibraryItem> childItems;
        private List<NodeItem> NodeItems;
        private int invalidItemCount;

    }

    static class Result {
        List<NodeItem> foldersItems;
    }

    public static interface Callback {

        void onProgressUpdate(NodeItem item);

        void onPostExecute(List<NodeItem> item);
    }
}
