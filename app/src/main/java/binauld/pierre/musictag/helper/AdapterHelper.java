package binauld.pierre.musictag.helper;


import android.content.Context;
import android.widget.ProgressBar;

import java.util.Comparator;

import binauld.pierre.musictag.adapter.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;

public class AdapterHelper {

    /**
     * Build the adapter used to adapt library item for the list view.
     * @param context The context when the adapter is executed.
     * @param comparator The comparator used to sort items.
     * @return The LibraryAdapter built.
     */
    public static LibraryItemAdapter buildAdapter(Context context, Comparator<LibraryItem> comparator) {
        return new LibraryItemAdapter(context, comparator);
    }
}
