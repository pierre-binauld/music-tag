package binauld.pierre.musictag.service;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.io.AsyncDrawable;
import binauld.pierre.musictag.io.DefaultArtworkLoader;
import binauld.pierre.musictag.io.ArtworkLoader;
import binauld.pierre.musictag.item.LibraryItem;

/**
 * Help to build artwork from audio file.
 */
public class ArtworkService {

    private int defaultArtworkResId;

    private BitmapDecoder defaultArtworkDecoder;

    private CacheService<Bitmap> cacheService;

    public ArtworkService(Context context, int defaultArtworkResId) {
        Resources res = context.getResources();

        this.defaultArtworkResId = defaultArtworkResId;

        this.cacheService = Locator.getCacheService();
        this.defaultArtworkDecoder = new ResourceBitmapDecoder(res, defaultArtworkResId);

        DefaultArtworkLoader loader = new DefaultArtworkLoader(this.cacheService, 0);
        loader.execute(this.defaultArtworkDecoder);
    }

    /**
     * Set the Artwork associated to item to the imageView.
     * If the Artwork has not been yet loaded, then it is loaded a placeholder is put in the image view while loading.
     * @param item Current item.
     * @param imageView Associated image view.
     */
    public void setArtwork(LibraryItem item, ImageView imageView, int artworkSize) {

        final String key = item.getDecoder().getKey(artworkSize, artworkSize);
//
        final Bitmap bitmap = cacheService.get(key);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(item, imageView)) {
            Resources res = imageView.getResources();
            final ArtworkLoader task = new ArtworkLoader(imageView, cacheService, defaultArtworkDecoder, artworkSize);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(res, cacheService.get(defaultArtworkResId), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(item);
        }



    }

    /**
     * Cancel a potential AsyncTask from anAsyncDrawable of the ImageView if the AsyncTask is outdated.
     * @param item The item that the artwork must be load.
     * @param imageView The image view which has to display artwork thumbnail.
     * @return False if the same work is in progress.
     */
    private boolean cancelPotentialWork(LibraryItem item, ImageView imageView) {
        final ArtworkLoader artworkLoader = AsyncDrawable.retrieveBitmapLoader(imageView);

        if (artworkLoader != null) {
            final LibraryItem taskItem = artworkLoader.getWorkingItem();
            // If bitmapData is not yet set or it differs from the new data
            if (taskItem == null || taskItem != item) {
                // Cancel previous task
                artworkLoader.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
}
