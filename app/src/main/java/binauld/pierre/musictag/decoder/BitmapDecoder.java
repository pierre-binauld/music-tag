package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public interface BitmapDecoder {

    /**
     * Decode the associated file to Bitmap.
     * @return
     */
    Bitmap decode();

    /**
     * Get the cache key of the Bitmap.
     * @return
     */
    String getKey();
}
