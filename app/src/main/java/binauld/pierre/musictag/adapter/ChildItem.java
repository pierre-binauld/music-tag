package binauld.pierre.musictag.adapter;


public abstract class ChildItem implements LibraryItem {

    protected NodeItem parent;

    public NodeItem getParent() {
        return parent;
    }

    public void setParent(NodeItem parent) {
        this.parent = parent;
    }
}
