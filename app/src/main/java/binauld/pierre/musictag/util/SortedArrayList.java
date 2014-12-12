package binauld.pierre.musictag.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortedArrayList<E> extends ArrayList<E> {

    private Comparator<E> comparator;

    public SortedArrayList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public void sortedInsert(E element) {
        this.add(element);
        Collections.sort(this, comparator);
    }

    public void sortedInsertAll(E... elements) {
        for (int i = 0; i < elements.length; i++) {
            this.add(elements[i]);
        }
        Collections.sort(this, comparator);
    }
}
