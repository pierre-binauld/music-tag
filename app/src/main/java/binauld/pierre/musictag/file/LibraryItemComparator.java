package binauld.pierre.musictag.file;


import org.jaudiotagger.tag.FieldKey;

import java.util.Comparator;
import java.util.Map;

public class LibraryItemComparator implements Comparator<Map<String, String>> {


    @Override
    public int compare(Map<String, String> song1, Map<String, String> song2) {
        return song1.get(FieldKey.TITLE.name()).compareTo(song2.get(FieldKey.TITLE.name()));
    }
}
