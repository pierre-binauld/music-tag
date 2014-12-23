package binauld.pierre.musictag.io;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

    private ProgressBar progressBar;

    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, Comparator<LibraryItem> comparator, int updateStep) {
        this.updateStep = updateStep;

        this.adapter = adapter;
        this.node = adapter.getCurrentNode();
        this.factory = libraryItemFactory;

        this.items = node.getChildren();
        this.comparator = comparator;
    }

    @Override
    protected Integer doInBackground(File... files) {
        this.initProgressBar(files);

        node.setState(LoadingState.LOADING);

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
                updateProgressBarOnUiThread();
                Log.w(this.getClass().toString(), e.getMessage());
            }
            node.setState(LoadingState.LOADED);
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
     * @param files The list of files.
     */
    private void initProgressBar(File[] files) {
        if (null != progressBar) {
            final int max = files.length;

            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(max);
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

}
