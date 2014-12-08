package binauld.pierre.musictag.file;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import binauld.pierre.musictag.file.factory.LibraryItemFactory;

public class LibraryItemLoader extends AsyncTask<File, Void, Integer> {

    public static int UPDATE_STEP = 5;

    private BaseAdapter adapter;
    private LibraryItemFilter filter;
    private List<LibraryItem> items;
    private Comparator<LibraryItem> comparator;
    private LibraryItemFactory libraryItemFactory;

    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, Comparator<LibraryItem> comparator, LibraryItemFilter filter) {
        this.adapter = adapter;
        this.filter = filter;
        this.items = adapter.getItems();
        this.comparator = comparator;
        this.libraryItemFactory = libraryItemFactory;
    }

    @Override
    protected Integer doInBackground(File... values) {
        int count = 0;
        int step = 0;

        for (int f = 0; f < values.length; f++) {
            File[] files = values[f].listFiles(filter);

            for (int i = 0; i < files.length; i++) {

                try {
                    LibraryItem item = libraryItemFactory.build(files[i]);
                    items.add(item);
                    step++;
                } catch (IOException e) {
                    Log.w(this.getClass().toString(), e.getMessage(), e);
                }

                if (step >= UPDATE_STEP || i == files.length - 1) {
                    Collections.sort(items, comparator);
                    publishProgress();
                    step = 0;
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
