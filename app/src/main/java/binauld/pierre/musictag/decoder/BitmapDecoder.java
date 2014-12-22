package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;

public interface BitmapDecoder {

    /**
     * Decode the associated file to Bitmap.
     * @return A bitmap.
     */
    Bitmap decode();

    /**
     * Get the cache key of the Bitmap.
     * @return The key..
     */
    String getId();
}
