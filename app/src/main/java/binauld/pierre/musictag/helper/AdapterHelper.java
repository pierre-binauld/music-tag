package binauld.pierre.musictag.helper;


import android.content.Context;

import java.util.Comparator;

import binauld.pierre.musictag.adapter.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;

public class AdapterHelper {

    public static LibraryItemAdapter buildAdapter(Context context, Comparator<LibraryItem> comparator) {
        LibraryItemAdapter adapter = new LibraryItemAdapter(context, comparator);
        return adapter;
    }
}
