package binauld.pierre.musictag.adapter;

/**
 * Implement the parent management from the LibraryItem interface.
 */
public abstract class ChildItem implements LibraryItem {

    protected NodeItem parent;

    @Override
    public NodeItem getParent() {
        return parent;
    }

    /**
     * Set the parent.
     * @param parent The parent to set.
     */
    public void setParent(NodeItem parent) {
        this.parent = parent;
    }
}
