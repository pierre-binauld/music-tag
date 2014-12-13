package binauld.pierre.musictag.io;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.helper.LoaderHelper;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.service.ThumbnailService;

public class LibraryItemLoaderManager {

    List<LibraryItemLoader> loaders = new ArrayList<LibraryItemLoader>();
    private LibraryItemAdapter adapter;
    private ThumbnailService thumbnailService;

    public LibraryItemLoaderManager(LibraryItemAdapter adapter, ThumbnailService thumbnailService) {
        this.adapter = adapter;
        this.thumbnailService = thumbnailService;
    }

    //TODO: Find a way to remove a loader when it is finished
    public LibraryItemLoader get() {
        LibraryItemLoader loader = LoaderHelper.buildLoader(adapter, thumbnailService);
        loaders.add(loader);
        return loader;
    }

    public void cancelAll(boolean mayInterruptIfRunning) {
        for (LibraryItemLoader loader : loaders) {
            loader.cancel(mayInterruptIfRunning);
        }
    }
}
