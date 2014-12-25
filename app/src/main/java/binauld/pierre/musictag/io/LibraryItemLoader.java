package binauld.pierre.musictag.io;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.collection.MultipleBufferedList;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.LoadingState;
import binauld.pierre.musictag.item.NodeItem;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryItemLoader extends AsyncTask<File, Void, Integer> {

    private int updateStep;

    private MultipleBufferedList<LibraryItem> items;
    private LibraryItemFactory factory;
    private int invalidItemCount;

    private LibraryItemAdapter adapter;
    private NodeItem node;
    private Comparator<LibraryItem> comparator;


    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, Comparator<LibraryItem> comparator, int updateStep) {
        this.updateStep = updateStep;

        this.adapter = adapter;
        this.node = adapter.getCurrentNode();
        this.factory = libraryItemFactory;

        this.items = node.getChildren();
        this.comparator = comparator;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        node.setState(LoadingState.LOADING);
    }

    @Override
    protected Integer doInBackground(File... files) {


        int j = 0;
        for (int i = 0; i < files.length; i++) {

            try {
                LibraryItem item = factory.build(files[i], node);

                this.items.add(item);

                j = ++j % updateStep;
                if (j == 0 || i == files.length - 1) {
                    Collections.sort(items.getWorkingList(), comparator);
                    items.push();
                    publishProgress();
                }
            } catch (IOException e) {
                invalidItemCount++;
                Log.w(this.getClass().toString(), e.getMessage());
            }
        }

        return items.size();
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
        items.pull();
        node.setInvalidItemCount(invalidItemCount);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        node.setState(LoadingState.LOADED);
        adapter.notifyDataSetChanged();
    }
}
