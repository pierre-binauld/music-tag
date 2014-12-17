package binauld.pierre.musictag.item;


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
    private List<LibraryItem> children = new ArrayList<LibraryItem>();
    private LoadingState state;

    public NodeItem(Comparator<LibraryItem> comparator) {
        this.init();
        this.comparator = comparator;
    }

    public NodeItem(NodeItem parent) {
        this.init();
        this.parent = parent;
        this.comparator = parent.getComparator();
    }

    private void init() {
        this.state = LoadingState.NOT_LOADED;
    }

    @Override
    public boolean getAudio() {
        return false;
    }

//    @Override
//    public Bitmap getThumbnail() {
//        return thumbnail;
//    }

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

    /**
     * Get the loading state from the item.
     * @return The loading state.
     */
    public LoadingState getState() {
        return state;
    }

    /**
     * Set the loading state to the item.
     * @param state The loading state.
     */
    public void setState(LoadingState state) {
        this.state = state;
    }
}
