package binauld.pierre.musictag.io;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * A Bitmap Drawable containing a bitmap loader.
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<ArtworkLoader> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, ArtworkLoader artworkLoader) {
        super(res, bitmap);
        bitmapWorkerTaskReference = new WeakReference<>(artworkLoader);
    }

    /**
     * Get the bitmap loader.
     * @return The bitmap loader.
     */
    ArtworkLoader getBitmapLoader() {
        return bitmapWorkerTaskReference.get();
    }

    /**
     * Retrieve bitmap loader from an ImageView.
     * @param imageView The image view containing the bitmap loader.
     * @return The bitmap loader or null if it does not exist.
     */
    public static ArtworkLoader retrieveBitmapLoader(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapLoader();
            }
        }
        return null;
    }
}
