package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represent a folder in a library.
 */
public abstract class NodeItem extends ChildItem {

    private Comparator<LibraryItem> comparator;
    private Bitmap thumbnail;
    private List<LibraryItem> children = new ArrayList<LibraryItem>();
    private boolean isLoaded;

    public NodeItem(Bitmap thumbnail, Comparator<LibraryItem> comparator) {
        this.init(thumbnail);
        this.comparator = comparator;
    }

    public NodeItem(Bitmap thumbnail, NodeItem parent) {
        this.init(thumbnail);
        this.parent = parent;
        this.comparator = parent.getComparator();
    }

    private void init(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
        this.isLoaded = false;
    }

    @Override
    public boolean isSong() {
        return false;
    }

    @Override
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void add(LibraryItem item) {
        children.add(item);
    }

    public void add(LibraryItem[] elements) {
        for (int i = 0; i < elements.length; i++) {
            children.add(elements[i]);
        }
        Collections.sort(children, comparator);
    }

    public int size() {
        return  children.size();
    }

    public LibraryItem getChild(int i) {
        return  children.get(i);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    public Comparator<LibraryItem> getComparator() {
        if(null != comparator) {
            return comparator;
        } else if (null != parent) {
            return parent.getComparator();
        } else {
            return null;
        }
    }

    public void setComparator(Comparator<LibraryItem> comparator) {
        this.comparator = comparator;
    }
}
