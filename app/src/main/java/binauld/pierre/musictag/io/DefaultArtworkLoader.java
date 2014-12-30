package binauld.pierre.musictag.io;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.service.CacheService;

/**
 * Allow to load artwork and put it in the cache. Perfect to load default artwork.
 */
public class DefaultArtworkLoader extends AsyncTask<BitmapDecoder, Void, Integer> {

    private CacheService<Bitmap> cacheService;
    private int artworkSize;

    public DefaultArtworkLoader(CacheService<Bitmap> cacheService, int artworkSize) {
        this.cacheService = cacheService;
        this.artworkSize = artworkSize;
    }

    @Override
    protected Integer doInBackground(BitmapDecoder... decoders) {
        for(BitmapDecoder decoder : decoders) {
            cacheService.put(decoder.getKey(artworkSize, artworkSize), decoder.decode());
        }
        return decoders.length;
    }
}
