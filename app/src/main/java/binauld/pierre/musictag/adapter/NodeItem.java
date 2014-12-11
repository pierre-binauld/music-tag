package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;

import java.io.File;

/**
 * Represent a folder in a library.
 */
public class NodeItem implements LibraryItem {

    private File file;
    private final Bitmap thumbnail;
    private String secondaryInformation;

    public NodeItem(File file, Bitmap thumbnail) {
        this.file = file;
        this.thumbnail = thumbnail;

        // TODO: Add local logic
        int fileNumber = file.list().length;
        this.secondaryInformation = fileNumber + " file";
        if(fileNumber > 1) {
            this.secondaryInformation += "s";
        }
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
        return secondaryInformation;
    }

    @Override
    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
