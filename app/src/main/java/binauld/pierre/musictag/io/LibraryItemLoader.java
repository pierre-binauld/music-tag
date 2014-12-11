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
public class LibraryItemLoader extends AsyncTask<File, LibraryItem, Integer> {

    private static int UPDATE_STEP = 5;

    private LibraryItemAdapter adapter;
    private FileFilter filter;
    private LibraryItemFactory factory;

    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, FileFilter filter) {
        this.adapter = adapter;
//        this.comparator = comparator;
        this.filter = filter;
        this.factory = libraryItemFactory;
    }

    @Override
    protected Integer doInBackground(File... values) {
        int count = 0;

        for (File value : values) {
            File[] files = value.listFiles(filter);
            if (null == files) {
                Log.w(this.getClass().toString(), "'"+value.getAbsolutePath()+"' does not contains readable file.");
            } else {
                LibraryItem[] items = new LibraryItem[UPDATE_STEP];
                for (int i = 0; i < files.length; i++) {

                    try {
                        items[i%UPDATE_STEP] = factory.build(files[i]);
                        if(hasToPublish(i, files.length)) {
                            publishProgress(items);
                        }
                    } catch (IOException e) {
                        Log.w(this.getClass().toString(), e.getMessage());
                    }
                }
            }
        }

        return count;
    }

    @Override
    protected void onProgressUpdate(LibraryItem... values) {
        adapter.add(values);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    private boolean hasToPublish(int i, int fileCount) {
        if(i > 0 && i % UPDATE_STEP == 0) {
            return true;
        } else if (i - 1 >= fileCount) {
            return true;
        } else {
            return false;
        }
    }
}
