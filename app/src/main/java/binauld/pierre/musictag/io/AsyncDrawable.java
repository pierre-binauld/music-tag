package binauld.pierre.musictag.io;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<ThumbnailLoader> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
                         ThumbnailLoader thumbnailLoader) {
        super(res, bitmap);
        bitmapWorkerTaskReference = new WeakReference<ThumbnailLoader>(thumbnailLoader);
    }

    public ThumbnailLoader getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }

    public static ThumbnailLoader getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
