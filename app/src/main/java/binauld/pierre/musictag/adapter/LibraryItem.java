package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;

/**
 * Item from audio library (Song or folder).
 */
public interface LibraryItem {

    /**
     * Check if the item contain an audio file.
     * @return Return true if it is an audio file.
     */
    public boolean isSong();

    /**
     * Get the primary information of the item (Like song title or folder name).
     * This information should be used for sorting and main display in the list.
     * @return A String of the primary information.
     */
    String getPrimaryInformation();

    /**
     * Get the primary information of the item (Like artist name)
     * This information should be displayed in addition to the main display.
     * @return A String of the secondary information.
     */
    String getSecondaryInformation();

    /**
     * Get the thumbnail of the item.
     * @return A Bitmap.
     */
    Bitmap getThumbnail();

    /**
     * Return the item's parent.
     * @return The parent.
     */
    NodeItem getParent();

}
