package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;

import java.io.File;

/**
 * Represent a folder in a library.
 */
public class NodeItem implements LibraryItem {

    private File file;

    public NodeItem(File file) {
        this.file = file;
    }

    @Override
    public boolean isSong() {
        return false;
    }

    @Override
    public String getPrimaryInformation() {
        return file.getName();
    }

    @Override
    public String getSecondaryInformation() {
        return "Folder";
    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }
}
