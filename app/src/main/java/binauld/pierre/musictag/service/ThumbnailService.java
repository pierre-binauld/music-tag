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

    private int defaultArtworkResID;
    private int folderResId;

    private BitmapDecoder defaultArtworkDecoder;
    private BitmapDecoder folderBitmapDecoder;

    private Bitmap defaultArtwork;

    private Cache<Bitmap> cache;

    //TODO: may be we can make one method getThumbnail which take the item as parameter ?
    public ThumbnailService(Cache<Bitmap> cache, Context context, int defaultArtworkResId, int folderResId) {
        Resources res = context.getResources();

        this.defaultArtworkResID = defaultArtworkResId;
        this.folderResId = folderResId;
        this.cache = cache;
        this.defaultArtworkDecoder = new ResourceBitmapDecoder(res, this.defaultArtworkResID);
        this.folderBitmapDecoder = new ResourceBitmapDecoder(res, folderResId);

        this.defaultArtwork = defaultArtworkDecoder.decode();

        BitmapDecoder[] decoders = new BitmapDecoder[2];
        decoders[0] = this.defaultArtworkDecoder;
        decoders[1] = this.folderBitmapDecoder;
        DefaultThumbnailLoader loader = new DefaultThumbnailLoader(cache);
        loader.execute(decoders);
    }


    /**
     * Build an artwork Bitmap from a source audio file.
     * If the source file does not contains an Artwork, then return an defined image.
     *
     * @param song The source audio file.
     * @return The artwork as Bitmap.
     */
//    public Bitmap getArtwork(AudioFile song) {
//        Artwork artworkTag = song.getTag().getFirstArtwork();
//        if (null == artworkTag) {
//            return defaultArtwork;
//        }
//
//        byte[] artworkData = artworkTag.getBinaryData();
//        Bitmap artwork = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length);
//        if (null == artwork) {
//            return defaultArtwork;
//        }
//
//        return artwork;
//    }

    /**
     * Get the folder thumbnail.
     * @return The folder thumbnail.
     */
//    public Bitmap getFolder() {
//        return folder;
//    }


    public void loadThumbnail(LibraryItem item, ImageView imageView) {

        final String key = item.getDecoder().getKey();
//
        final Bitmap bitmap = cache.get(key);
        if (bitmap != null) {
//            imageView.setImageBitmap(defaultArtwork);
            imageView.setImageBitmap(bitmap);
        } if (cancelPotentialWork(item, imageView)) {
            Resources res = imageView.getResources();
            final ThumbnailLoader task = new ThumbnailLoader(imageView, cache, defaultArtworkDecoder);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(res, cache.get(defaultArtworkResID), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(item);
        }



    }

    private boolean cancelPotentialWork(LibraryItem item, ImageView imageView) {
        final ThumbnailLoader thumbnailLoader = AsyncDrawable.getBitmapWorkerTask(imageView);

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

    public BitmapDecoder getFolderBitmapDecoder() {
        return folderBitmapDecoder;
    }
}
