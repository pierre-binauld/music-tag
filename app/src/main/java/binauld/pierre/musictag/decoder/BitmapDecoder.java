package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;

public interface BitmapDecoder {

    Bitmap decode();

    String getKey();

//    int getDefaultSourceResId();
}
