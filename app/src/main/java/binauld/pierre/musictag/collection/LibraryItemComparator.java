package binauld.pierre.musictag.collection;


import java.util.Comparator;

import binauld.pierre.musictag.item.LibraryItem;

/**
 * Allows to compare 2 library items.
 * Sort files in alphabetical order (Folder first).
 */
public class LibraryItemComparator implements Comparator<LibraryItem> {

    @Override
    public int compare(LibraryItem item1, LibraryItem item2) {
        if(!item1.isAudioItem() && item2.isAudioItem()) {
            return -1;
        } else if (item1.isAudioItem() && !item2.isAudioItem()) {
            return 1;
        } else {
            return item1.getItemable().getPrimaryInformation().toLowerCase()
                    .compareTo(item2.getItemable().getPrimaryInformation().toLowerCase());
        }
    }
}
