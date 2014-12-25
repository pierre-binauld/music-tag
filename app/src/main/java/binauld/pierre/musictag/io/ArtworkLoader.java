package binauld.pierre.musictag.io;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.service.CacheService;

public class ArtworkLoader extends AsyncTask<LibraryItem, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final BitmapDecoder defaultThumbnailDecoder;
    private LibraryItem item;
    private CacheService<Bitmap> cacheService;
    private int thumbnailSize;

    public ArtworkLoader(ImageView imageView, CacheService<Bitmap> cacheService, BitmapDecoder defaultThumbnailDecoder, int thumbnailSize) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<>(imageView);
        this.cacheService = cacheService;
        this.defaultThumbnailDecoder = defaultThumbnailDecoder;
        this.thumbnailSize = thumbnailSize;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(LibraryItem... items) {
        item = items[0];
        BitmapDecoder decoder = item.getDecoder();

        String key = decoder.getKey(thumbnailSize, thumbnailSize);
        Bitmap bitmap = decoder.decode(thumbnailSize, thumbnailSize);
        if (null != bitmap) {
            cacheService.put(key, bitmap);
        } else if (decoder != defaultThumbnailDecoder) {
            item.switchDecoder(defaultThumbnailDecoder);
            bitmap = this.doInBackground(item);
        }

        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
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
