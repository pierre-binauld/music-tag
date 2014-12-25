package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;

public interface BitmapDecoder {

    /**
     * Decode the associated file to Bitmap.
     * @return A bitmap.
     */
    Bitmap decode();

    /**
     * Decode the associated file to Bitmap.
     * @param targetedWidth The targeted width of the bitmap wanted.
     * @param targetedHeight The targeted height of the bitmap wanted.
     * @return A bitmap.
     */
    Bitmap decode(int targetedWidth, int targetedHeight);

    /**
     * Get the cache key of the Bitmap.
     * @return The key.
     */
    String getKey(int targetedWidth, int targetedHeight);
}
