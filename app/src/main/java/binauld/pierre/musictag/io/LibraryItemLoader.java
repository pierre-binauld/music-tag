package binauld.pierre.musictag.io;

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
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.LoadingState;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryItemLoader extends AsyncTask<LibraryItem, LibraryItemLoader.Progress, LibraryItemLoader.Result> {

    private int updateStep;
    private boolean drillDown;

    //    private MultipleBufferedList<LibraryItem> items;
//    private MultipleBufferedList<FolderItem> folderItems;
    private LibraryItemFactory factory;
//    private int invalidItemCount;

    //    private LibraryItemAdapter adapter;
//    private FolderItem rootItem;
    private Comparator<LibraryItem> comparator;

    private Callback callback;


    public LibraryItemLoader(/*FolderItem node,*/ LibraryItemFactory libraryItemFactory, Comparator<LibraryItem> comparator, int updateStep, boolean drillDown, Callback callback) {
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

        List<FolderItem> items = new ArrayList<>();
        for (LibraryItem item : params) {
            if (!item.isAudioItem()) {
                items.add((FolderItem) item);
            }
        }

        for (FolderItem item : items) {
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
//            progress.folderItems.pull();
            progress.currentItem.setInvalidItemCount(progress.invalidItemCount);
            progress.currentItem.setState(progress.state);
            callback.onProgressUpdate(progress.rootItem);
        }
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Result result) {
//        items.pull();
//        folderItems.pull();
//        rootItem.setState(LoadingState.LOADED);
        callback.onPostExecute(result.foldersItems);
//        adapter.notifyDataSetChanged();
    }

    public void loadItemTree(FolderItem rootItem) {
        Queue<FolderItem> queue = new LinkedList<>();
        queue.add(rootItem);

        do {
            FolderItem currentFolderItem = queue.poll();

//            int invalidItemCount = 0;
            if (currentFolderItem.getState() == LoadingState.LOADING) {
                queue.add(currentFolderItem);
            } else {
                if (currentFolderItem.getState() == LoadingState.NOT_LOADED) {
                    currentFolderItem.setState(LoadingState.LOADING);

                    loadItem(rootItem, currentFolderItem);
                }

                queue.addAll(currentFolderItem.getFolderItems());
            }
        } while (!queue.isEmpty() && drillDown);
    }

    public void loadItem(FolderItem rootItem, FolderItem currentFolderItem) {

        int invalidItemCount = 0;

        MultipleBufferedList<LibraryItem> currentItems = currentFolderItem.getChildren();
        List<FolderItem> currentFolderItems = currentFolderItem.getFolderItems();
        File[] currentFiles = currentFolderItem.getFileList();

        if (null != currentFiles) {

            int j = 0;
            for (int i = 0; i < currentFiles.length; i++) {

                try {
                    LibraryItem item = factory.build(currentFiles[i], currentFolderItem);

                    if (!item.isAudioItem()) {
                        FolderItem folderItem = (FolderItem) item;
                        currentFolderItems.add(folderItem);
                    }
                    currentItems.add(item);

                } catch (IOException e) {
                    invalidItemCount++;
                    Log.w(this.getClass().toString(), e.getMessage());
                }

                j = ++j % updateStep;
                if (j == 0 || i == currentFiles.length - 1) {
                    //TODO: folderItem.getComparator();
                    Collections.sort(currentItems.getWorkingList(), comparator);
                    currentItems.push();
//                    currentFolderItems.push();
                    Progress progress = new Progress();
                    progress.rootItem = rootItem;
                    progress.currentItem = currentFolderItem;
                    progress.childItems = currentItems;
                    progress.folderItems = currentFolderItems;
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
        private FolderItem rootItem;
        private FolderItem currentItem;
        private MultipleBufferedList<LibraryItem> childItems;
        private List<FolderItem> folderItems;
        private int invalidItemCount;

    }

    static class Result {
        List<FolderItem> foldersItems;
    }

    public static interface Callback {

        void onProgressUpdate(FolderItem item);

        void onPostExecute(List<FolderItem> item);
    }
}
