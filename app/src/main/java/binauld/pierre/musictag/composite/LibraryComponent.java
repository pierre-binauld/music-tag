package binauld.pierre.musictag.composite;


import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.visitor.ComponentVisitor;

/**
 * Item from audio library (Song or folder).
 */
public interface LibraryComponent {

    /**
     * Check if the item contain an audio file.
     * @return Return true if it is an audio file.
     */
//    public boolean isAudioItem();

    /**
     * Return the item's parent.
     * @return The parent.
     */
    LibraryComposite getParent();

    Item getItem();

    void accept(ComponentVisitor visitor);

//    public boolean isAudioItem();
}
