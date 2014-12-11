package binauld.pierre.musictag.helper;

import java.io.FileFilter;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.factory.FileFilterFactory;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.io.LibraryItemComparator;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Help to build the AsyncTask loading library items list.
 */
public class LoaderHelper {

    /**
     * Help to build the AsyncTask loading library items list.
     * @param adapter The adapter used to adapt library items for the list view.
     * @return The loader built.
     */
    public static LibraryItemLoader buildAlphabeticalLoader(LibraryItemAdapter adapter, ThumbnailService thumbnailService) {
        FileFilterFactory filterFactory = new FileFilterFactory();

        FileFilter filter = filterFactory.build();

        LibraryItemFactory factory = new LibraryItemFactory(thumbnailService);

        return new LibraryItemLoader(adapter, factory, filter);
    }
}
