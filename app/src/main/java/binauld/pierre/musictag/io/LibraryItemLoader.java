package binauld.pierre.musictag.io;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import binauld.pierre.musictag.adapter.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.factory.LibraryItemFactory;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryItemLoader extends AsyncTask<File, Void, Integer> {

    private static int UPDATE_STEP = 5;

    private BaseAdapter adapter;
    private FileFilter filter;
    private Comparator<LibraryItem> comparator;
    private LibraryItemFactory factory;

    private List<LibraryItem> items;

    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, Comparator<LibraryItem> comparator, FileFilter filter) {
        this.adapter = adapter;
        this.comparator = comparator;
        this.filter = filter;
        this.factory = libraryItemFactory;
        this.items = adapter.getItems();
    }

    @Override
    protected Integer doInBackground(File... values) {
        int count = 0;
        int step = 0;

        for (File value : values) {
            File[] files = value.listFiles(filter);
            if (null == files) {
                Log.w(this.getClass().toString(), "'"+value.getAbsolutePath()+"' does not contains readable files.");
            } else {
                for (int i = 0; i < files.length; i++) {

                    try {
                        LibraryItem item = factory.build(files[i]);
                        items.add(item);
                        step++;
                    } catch (IOException e) {
                        Log.w(this.getClass().toString(), e.getMessage());
                    }

                    if (step >= UPDATE_STEP || i == files.length - 1) {
                        Collections.sort(items, comparator);
                        publishProgress();
                        step = 0;
                    }
                }
            }
        }

        return count;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}
