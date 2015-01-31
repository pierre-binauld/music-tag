package binauld.pierre.musictag.item.itemable;

import binauld.pierre.musictag.decoder.BitmapDecoder;

public interface Itemable {

    /**
     * Get the primary information of the item (Like list_item_placeholder title or folder name).
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
     * Get the bitmap decoder.
     * @return The bitmap decoder.
     */
    BitmapDecoder getBitmapDecoder();

    /**
     * Allow to change the bitmap decoder.
     * @param bitmapDecoder The bitmap decoder.
     */
    void setBitmapDecoder(BitmapDecoder bitmapDecoder);

}
