package binauld.pierre.musictag.task;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.Item;

/**
 * Load an artwork in background.
 */
public class ArtworkLoader extends AsyncTask<Item, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final BitmapDecoder defaultArtworkDecoder;
    private Item item;
    private LruCache<String, Bitmap> cache;
    private int artworkSize;

    public ArtworkLoader(ImageView imageView, LruCache<String, Bitmap> cache, BitmapDecoder defaultArtworkDecoder, int artworkSize) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<>(imageView);
        this.cache = cache;
        this.defaultArtworkDecoder = defaultArtworkDecoder;
        this.artworkSize = artworkSize;
    }

    @Override
    protected Bitmap doInBackground(Item... items) {
        item = items[0];
        BitmapDecoder decoder = item.getBitmapDecoder();

        String key = decoder.getKey(artworkSize, artworkSize);
        Bitmap bitmap = decoder.decode(artworkSize, artworkSize);
        if (null != bitmap) {
            cache.put(key, bitmap);
        } else if (decoder != defaultArtworkDecoder) {
            item.setBitmapDecoder(defaultArtworkDecoder);
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
    public Item getWorkingItem() {
        return item;
    }

}
