package binauld.pierre.musictag.item;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Comparator;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;

public class FolderItem extends NodeItem {

    private File file;
    private String secondaryInformation;

    public FolderItem(File file, Comparator<LibraryItem> comparator) {
        super(comparator);
        init(file);
    }

    public FolderItem(File file, NodeItem parent) {
        super(parent);
        init(file);
    }

    private void init(File file) {
        this.file = file;

        // TODO: Add local logic
        int fileNumber = file.list().length;
        this.secondaryInformation = fileNumber + " file";
        if(fileNumber > 1) {
            this.secondaryInformation += "s";
        }
    }

    @Override
    public String getPrimaryInformation() {
        return file.getName();
    }

    @Override
    public String getSecondaryInformation() {
        return secondaryInformation;
    }

    /**
     * Get the file.
     * @return The file.
     */
    public File getFile() {
        return file;
    }

}
