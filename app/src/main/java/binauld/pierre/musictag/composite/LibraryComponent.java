package binauld.pierre.musictag.composite;


import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.visitor.ComponentVisitor;

/**
 * Item from audio library (Song or folder).
 */
public interface LibraryComponent {
    /**
     * Return the item's parent.
     * @return The parent.
     */
    LibraryComposite getParent();

    Item getItem();

    void accept(ComponentVisitor visitor);

}
