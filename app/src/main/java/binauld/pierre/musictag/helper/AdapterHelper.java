package binauld.pierre.musictag.helper;


import android.content.Context;

import java.util.Comparator;

import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.service.ThumbnailService;

public class AdapterHelper {

    /**
     * Build the adapter used to adapt library item for the list view.
     * @param context The context when the adapter is executed.
     * @param comparator The comparator used to sort items.
     * @return The LibraryAdapter built.
     */
    public static LibraryItemAdapter buildAdapter(Context context, ThumbnailService thumbnailService, Comparator<LibraryItem> comparator) {
        return new LibraryItemAdapter(context, thumbnailService, comparator);
    }
}
