package binauld.pierre.musictag.io;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import binauld.pierre.musictag.adapter.LibraryItemComparator;
import binauld.pierre.musictag.collection.MultipleBufferedList;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.item.LoadingState;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.factory.LibraryItemFactory;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryItemLoader extends AsyncTask<FolderItem, Void, Integer> {

    //TODO: Parameterize this for different screen size
    private static int UPDATE_STEP = 10;

    private final NodeItem node;
    private final LibraryItemLoaderManager manager;
    private LibraryItemAdapter adapter;
    private FileFilter filter;
    private LibraryItemFactory factory;
    private ProgressBar progressBar;

    private MultipleBufferedList<LibraryItem> items;
    private int invalidItemCount;
    private Comparator<LibraryItem> comparator;

    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, FileFilter filter, Comparator<LibraryItem> comparator, LibraryItemLoaderManager manager) {
        this.adapter = adapter;
        this.node = adapter.getCurrentNode();
        this.filter = filter;
        this.factory = libraryItemFactory;
        this.manager = manager;

        this.items = this.node.getChildren();
        this.comparator = comparator;
    }

    @Override
    protected Integer doInBackground(FolderItem... values) {
        this.initProgressBar(values);

        for (FolderItem folderItem : values) {
            folderItem.setState(LoadingState.LOADING);
            File folder = folderItem.getFile();
            File[] files = folder.listFiles(filter);

            if (null == files) {
                Log.w(this.getClass().toString(), "'" + folder.getAbsolutePath() + "' does not contains readable file.");
            } else {
                int j = 0;
                for (int i = 0; i < files.length; i++) {

                    try {
                        LibraryItem item = factory.build(files[i], folderItem);

                        this.items.add(item);

                        j = ++j % UPDATE_STEP;
                        if (j == 0 || i == files.length - 1) {
                            Collections.sort(this.items.getTail(), comparator);
                            this.items.push();
                            publishProgress();
                        }
                    } catch (IOException e) {
                        invalidItemCount++;
                        updateProgressBarOnUiThread();
                        Log.w(this.getClass().toString(), e.getMessage());
                    }
                }
            }
            folderItem.setState(LoadingState.LOADED);
        }

        return items.size();
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
        items.pull();
        updateProgressBar();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Initialize the max of progress bar with the list of folder items.
     *
     * @param items The list of folder items.
     */
    private void initProgressBar(FolderItem[] items) {
        if (null != progressBar) {
            int max = 0;
            for (FolderItem folder : items) {
                max += folder.getFile().list().length;
            }

            final int finalMax = max;
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(finalMax);
                }
            });
        }
    }

    /**
     * Update the progress bar from any thread.
     * Used in case of unreadable file.
     */
    private void updateProgressBarOnUiThread() {
        if (null != progressBar) {
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    updateProgressBar();
                }
            });
        }
    }

    /**
     * Update the progress bar with the items count and the invalid item count.
     */
    public void updateProgressBar() {
        if (null != progressBar) {
            progressBar.setProgress(items.size() + invalidItemCount);
        }
    }

    /**
     * Set the progress bar used to display the loading progression.
     *
     * @param progressBar The progress bar.
     */
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * Get the progress bar used to display the loading progression.
     *
     * @return The progress bar.
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
