package binauld.pierre.musictag.file;


import android.graphics.Bitmap;

public interface LibraryItem {
    public boolean isSong();

    String getPrimaryInformation();

    String getSecondaryInformation();

    Bitmap getThumbnail();
}
