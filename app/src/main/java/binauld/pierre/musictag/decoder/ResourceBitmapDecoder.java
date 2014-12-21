package binauld.pierre.musictag.decoder;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceBitmapDecoder implements BitmapDecoder {

    private Resources res;
    private int id;

    public ResourceBitmapDecoder(Resources res, int id) {
        this.res = res;
        this.id = id;
    }

    @Override
    public Bitmap decode() {
        return BitmapFactory.decodeResource(res, id);
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }
}
