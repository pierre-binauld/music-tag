package binauld.pierre.musictag.file;


import android.graphics.Bitmap;

import java.io.File;

public class NodeItem implements LibraryItem{

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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
