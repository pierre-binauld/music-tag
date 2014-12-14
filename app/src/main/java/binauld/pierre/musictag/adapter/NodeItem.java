package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;

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
        Collections.addAll(children, elements);
        Collections.sort(children, comparator);
    }

    /**
     * Get the number of children node.
     * @return The number of children node.
     */
    public int size() {
        return children.size();
    }

    /**
     * Get a child.
     * @param i Index of the child.
     * @return A child.
     */
    public LibraryItem getChild(int i) {
        return children.get(i);
    }

    /**
     * Check if the node is loaded or loading.
     * @return
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Set the loaded state of the node.
     * @param isLoaded The loaded state.
     */
    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    /**
     * Get the comparator used to sort children.
     * @return The comparator.
     */
    public Comparator<LibraryItem> getComparator() {
        if(null != comparator) {
            return comparator;
        } else if (null != parent) {
            return parent.getComparator();
        } else {
            return null;
        }
    }

    /**
     * Set the comparator used to sort children.
     * @param comparator The comparator.
     */
    public void setComparator(Comparator<LibraryItem> comparator) {
        this.comparator = comparator;
    }
}
