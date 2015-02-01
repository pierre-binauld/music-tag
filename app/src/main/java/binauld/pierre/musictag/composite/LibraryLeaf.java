package binauld.pierre.musictag.composite;

import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.visitor.ComponentVisitor;

/**
 * Implement the parent management from the LibraryItem interface.
 */
public class LibraryLeaf implements LibraryComponent {

    protected LibraryComposite parent;
    protected Item item;

    public LibraryLeaf(Item item) {
        this.item = item;
    }

    @Override
    public LibraryComposite getParent() {
        return parent;
    }

    /**
     * Set the parent.
     *
     * @param parent The parent to set.
     */
    public void setParent(LibraryComposite parent) {
        this.parent = parent;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public void accept(ComponentVisitor visitor) {
        visitor.visit(this);
    }

}
