package binauld.pierre.musictag.service;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.task.ArtworkLoader;
import binauld.pierre.musictag.task.AsyncDrawable;
import binauld.pierre.musictag.task.DefaultArtworkLoader;

/**
 * Help to build artwork from audio file.
 */
public class ArtworkService {

    private BitmapDecoder defaultArtworkDecoder;

    private CacheService<Bitmap> cacheService;

    public ArtworkService(BitmapDecoder defaultArtworkDecoder) {
        this.cacheService = Locator.getCacheService();
        this.defaultArtworkDecoder = defaultArtworkDecoder;
    }

    public void initDefaultArtwork(int artworkSize) {
        DefaultArtworkLoader loader = new DefaultArtworkLoader(this.cacheService, artworkSize);
        loader.execute(this.defaultArtworkDecoder);
    }

    /**
     * Set the Artwork associated to item to the imageView.
     * If the Artwork has not been yet loaded, then it is loaded a placeholder is put in the image view while loading.
     *
     * @param item      Current item.
     * @param imageView Associated image view.
     */
    public void setArtwork(Item item, ImageView imageView, int artworkSize) {

        final String key = item.getBitmapDecoder().getKey(artworkSize, artworkSize);

        final Bitmap bitmap = cacheService.get(key);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(item, imageView)) {
            Resources res = imageView.getResources();
            final ArtworkLoader task = new ArtworkLoader(imageView, cacheService, defaultArtworkDecoder, artworkSize);
            Bitmap placeholder = cacheService.get(defaultArtworkDecoder.getKey(artworkSize, artworkSize));
            final AsyncDrawable asyncDrawable = new AsyncDrawable(res, placeholder, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(item);
        }
    }

    /**
     * Cancel a potential AsyncTask from anAsyncDrawable of the ImageView if the AsyncTask is outdated.
     *
     * @param item      The item that the artwork must be load.
     * @param imageView The image view which has to display artwork thumbnail.
     * @return False if the same work is in progress.
     */
    private boolean cancelPotentialWork(Item item, ImageView imageView) {
        final ArtworkLoader artworkLoader = AsyncDrawable.retrieveBitmapLoader(imageView);

        if (artworkLoader != null) {
            final Item taskItem = artworkLoader.getWorkingItem();
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
