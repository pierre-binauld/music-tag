package binauld.pierre.musictag.io;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.service.CacheService;

/**
 * Load an artwork in background.
 */
public class ArtworkLoader extends AsyncTask<LibraryItem, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final BitmapDecoder defaultArtworkDecoder;
    private LibraryItem item;
    private CacheService<Bitmap> cacheService;
    private int artworkSize;

    public ArtworkLoader(ImageView imageView, CacheService<Bitmap> cacheService, BitmapDecoder defaultArtworkDecoder, int artworkSize) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<>(imageView);
        this.cacheService = cacheService;
        this.defaultArtworkDecoder = defaultArtworkDecoder;
        this.artworkSize = artworkSize;
    }

    @Override
    protected Bitmap doInBackground(LibraryItem... items) {
        item = items[0];
        BitmapDecoder decoder = item.getDecoder();

        String key = decoder.getKey(artworkSize, artworkSize);
        Bitmap bitmap = decoder.decode(artworkSize, artworkSize);
        if (null != bitmap) {
            cacheService.put(key, bitmap);
        } else if (decoder != defaultArtworkDecoder) {
            item.switchDecoder(defaultArtworkDecoder);
            bitmap = this.doInBackground(item);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        final ImageView imageView = imageViewReference.get();
        if (imageView != null && bitmap != null) {
            final ArtworkLoader artworkLoader = AsyncDrawable.retrieveBitmapLoader(imageView);
            if (this == artworkLoader) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * Get the item which loader working on.
     * @return The item.
     */
    public LibraryItem getWorkingItem() {
        return item;
    }

}
