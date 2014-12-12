package binauld.pierre.musictag.adapter;


import java.util.Comparator;

import binauld.pierre.musictag.adapter.LibraryItem;

/**
 * Allows to compare 2 library items.
 * Sort files in alphabetical order (Folder first).
 */
public class LibraryItemComparator implements Comparator<LibraryItem> {

    @Override
    public int compare(LibraryItem item1, LibraryItem item2) {
        if(!item1.isSong() && item2.isSong()) {
            return -1;
        } else if (item1.isSong() && !item2.isSong()) {
            return 1;
        } else {
            return item1.getPrimaryInformation().toLowerCase()
                    .compareTo(item2.getPrimaryInformation().toLowerCase());
        }
    }
}
