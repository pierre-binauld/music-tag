package binauld.pierre.musictag.task;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;

import binauld.pierre.musictag.decoder.BitmapDecoder;

/**
 * Allow to load artwork and put it in the cache. Perfect to load default artwork.
 */
public class DefaultArtworkLoader extends AsyncTask<BitmapDecoder, Void, Integer> {

    private LruCache<String, Bitmap> cache;
    private int artworkSize;

    public DefaultArtworkLoader(LruCache<String, Bitmap> cache, int artworkSize) {
        this.cache = cache;
        this.artworkSize = artworkSize;
    }

    @Override
    protected Integer doInBackground(BitmapDecoder... decoders) {
        for(BitmapDecoder decoder : decoders) {
            cache.put(decoder.getKey(artworkSize, artworkSize), decoder.decode());
        }
        return decoders.length;
    }
}
