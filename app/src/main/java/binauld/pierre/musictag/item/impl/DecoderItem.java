package binauld.pierre.musictag.item.impl;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.Item;

public abstract class DecoderItem implements Item {

    private BitmapDecoder decoder;

    @Override
    public BitmapDecoder getBitmapDecoder() {
        return decoder;
    }

    @Override
    public void setBitmapDecoder(BitmapDecoder bitmapDecoder) {
        this.decoder = bitmapDecoder;
    }
}
