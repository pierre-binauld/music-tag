package binauld.pierre.musictag.composite;


import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.collection.MultipleBufferedList;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.visitor.ComponentVisitor;

/**
 * Represent a folder in a library.
 */
public class LibraryComposite extends LibraryLeaf {

    private MultipleBufferedList<LibraryComponent> children;
    private LoadingState state;
    private int invalidComponentCount;
//    private List<LibraryComposite> nodeItems = new ArrayList<>();

    public LibraryComposite(Item item) {
        this(item, null);
    }

    public LibraryComposite(Item item, LibraryComposite parent) {
        super(item);
        this.parent = parent;
        this.children = new MultipleBufferedList<>(new MultipleBufferedList.ListFactory<LibraryComponent>() {
            public List<LibraryComponent> buildEmptyList() {
                return new ArrayList<>();
            }
        });
        this.state = LoadingState.NOT_LOADED;
    }

//    @Override
//    public boolean isAudioItem() {
//        return false;
//    }

    /**
     * Get the maximum number of possible children.
     * @return The maximum number of possible children.
     */
//    public abstract int getMaxChildrenCount();

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
    public LibraryComponent getChild(int i) {
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
    public MultipleBufferedList<LibraryComponent> getChildren() {
        return children;
    }

    /**
     * Set the count of invalid item found by a loader.
     * @param invalidItemCount The count of invalid item found.
     */
    public void setInvalidComponentCount(int invalidItemCount) {
        this.invalidComponentCount = invalidItemCount;
    }
    /**
     * Get the count of invalid item found by a loader.
     * @return The count of invalid item found.
     */
    public int getInvalidComponentCount() {
        return invalidComponentCount;
    }

//    @Override
//    public boolean isAudioItem() {
//        return false;
//    }

//    public List<LibraryComposite> getNodeItems() {
//        return nodeItems;
//    }

    @Override
    public void accept(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}
