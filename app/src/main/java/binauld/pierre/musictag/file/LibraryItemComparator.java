package binauld.pierre.musictag.file;


import java.util.Comparator;

public class LibraryItemComparator implements Comparator<LibraryItem> {

    @Override
    public int compare(LibraryItem item1, LibraryItem item2) {
        if(!item1.isSong() && item2.isSong()) {
            return -1;
        } else if (item1.isSong() && !item2.isSong()) {
            return 1;
        } else {
            return item1.getPrimaryInformation()
                    .compareTo(item2.getPrimaryInformation());
        }
    }
}
