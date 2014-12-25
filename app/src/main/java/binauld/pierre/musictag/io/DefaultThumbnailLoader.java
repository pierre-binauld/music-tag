package binauld.pierre.musictag.io;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import binauld.pierre.musictag.decoder.BitmapDecoder;

public class DefaultThumbnailLoader extends AsyncTask<BitmapDecoder, Void, Integer> {

    private Cache<Bitmap> cache;
    private int thumbnailSize;

    public DefaultThumbnailLoader(Cache<Bitmap> cache, int thumbnailSize) {
        this.cache = cache;
        this.thumbnailSize = thumbnailSize;
    }

    @Override
    protected Integer doInBackground(BitmapDecoder... decoders) {
        for(BitmapDecoder decoder : decoders) {
            cache.put(decoder.getKey(thumbnailSize, thumbnailSize), decoder.decode());
        }
        return decoders.length;
    }
}
