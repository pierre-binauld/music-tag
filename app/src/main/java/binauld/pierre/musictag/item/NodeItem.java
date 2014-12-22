package binauld.pierre.musictag.item;


import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.collection.MultipleBufferedList;

/**
 * Represent a folder in a library.
 */
public abstract class NodeItem extends ChildItem {

    private MultipleBufferedList<LibraryItem> children;
    private LoadingState state;

    public NodeItem() {
        this.init();
    }

    public NodeItem(NodeItem parent) {
        this.init();
        this.parent = parent;
    }

    private void init() {
        this.children = new MultipleBufferedList<>(new MultipleBufferedList.ListFactory<LibraryItem>() {
            public List<LibraryItem> buildEmptyList() {
                return new ArrayList<>();
            }
        });
        this.state = LoadingState.NOT_LOADED;
    }

    @Override
    public boolean isAudioItem() {
        return false;
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

    /**
     * Get the list of children for modification.
     * @return The list of children
     */
    public MultipleBufferedList<LibraryItem> getChildren() {
        return children;
    }
}
