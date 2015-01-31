package binauld.pierre.musictag.item;


import binauld.pierre.musictag.item.itemable.Itemable;

/**
 * Item from audio library (Song or folder).
 */
public interface LibraryItem {

    /**
     * Check if the item contain an audio file.
     * @return Return true if it is an audio file.
     */
//    public boolean isAudioItem();

    /**
     * Return the item's parent.
     * @return The parent.
     */
    NodeItem getParent();

    Itemable getItemable();



    public boolean isAudioItem();
}
