package binauld.pierre.musictag.service;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.io.AsyncDrawable;
import binauld.pierre.musictag.io.Cache;
import binauld.pierre.musictag.io.DefaultThumbnailLoader;
import binauld.pierre.musictag.io.ThumbnailLoader;
import binauld.pierre.musictag.item.LibraryItem;

/**
 * Help to build artwork from audio file.
 */
public class ThumbnailService {

    private int defaultThumbnailResId;

    private BitmapDecoder defaultThumbnailDecoder;

    private Cache<Bitmap> cache;

    public ThumbnailService(Cache<Bitmap> cache, Context context, int defaultThumbnailResId) {
        Resources res = context.getResources();

        this.defaultThumbnailResId = defaultThumbnailResId;

        this.cache = cache;
        this.defaultThumbnailDecoder = new ResourceBitmapDecoder(res, defaultThumbnailResId);

        DefaultThumbnailLoader loader = new DefaultThumbnailLoader(this.cache, 0);
        loader.execute(this.defaultThumbnailDecoder);
    }

    /**
     * Set the thumbnail associated to item to the imageView.
     * If the thumbnail has not been yet loaded, then it is loaded a placeholder is put in the image view while loading.
     * @param item Current item.
     * @param imageView Associated image view.
     */
    public void setThumbnail(LibraryItem item, ImageView imageView, int thumbnailSize) {

        final String key = item.getDecoder().getKey(thumbnailSize, thumbnailSize);
//
        final Bitmap bitmap = cache.get(key);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(item, imageView)) {
            Resources res = imageView.getResources();
            final ThumbnailLoader task = new ThumbnailLoader(imageView, cache, defaultThumbnailDecoder, thumbnailSize);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(res, cache.get(defaultThumbnailResId), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(item);
        }



    }

    /**
     * Cancel a potential AsyncTask from anAsyncDrawable of the ImageView if the AsyncTask is outdated.
     * @param item The item that the thumbnail must be load.
     * @param imageView The image view which has to display thmbnail.
     * @return False if the same work is in progress.
     */
    private boolean cancelPotentialWork(LibraryItem item, ImageView imageView) {
        final ThumbnailLoader thumbnailLoader = AsyncDrawable.retrieveBitmapLoader(imageView);

        if (thumbnailLoader != null) {
            final LibraryItem taskItem = thumbnailLoader.getWorkingItem();
            // If bitmapData is not yet set or it differs from the new data
            if (taskItem == null || taskItem != item) {
                // Cancel previous task
                thumbnailLoader.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
}
