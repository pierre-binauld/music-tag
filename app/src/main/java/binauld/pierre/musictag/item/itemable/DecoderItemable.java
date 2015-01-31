package binauld.pierre.musictag.item.itemable;

import binauld.pierre.musictag.decoder.BitmapDecoder;

public abstract class DecoderItemable implements Itemable {

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
