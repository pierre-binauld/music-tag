package binauld.pierre.musictag.helper;

import android.content.res.Resources;

import java.io.FileFilter;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.factory.FileFilterFactory;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Help to build the AsyncTask loading library items list.
 */
public class LoaderHelper {

    /**
     * Help to build the AsyncTask loading library items list.
     * @param adapter The adapter used to adapt library items for the list view.
     * @param res
     * @param manager The manager to the loader.
     * @return The loader built.
     */
    public static LibraryItemLoader buildLoader(LibraryItemAdapter adapter, ThumbnailService thumbnailService, LibraryItemLoaderManager manager) {
        FileFilterFactory filterFactory = new FileFilterFactory();

        FileFilter filter = filterFactory.build();

        LibraryItemFactory factory = new LibraryItemFactory(thumbnailService.getFolderBitmapDecoder());

        return new LibraryItemLoader(adapter, factory, filter, manager);
    }
}
