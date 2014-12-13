package binauld.pierre.musictag.adapter;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Comparator;

public class FolderItem extends NodeItem {

    private File file;
    private String secondaryInformation;

    public FolderItem(File file, Comparator<LibraryItem> comparator) {
        super(null, comparator);
        init(file);
    }

    public FolderItem(File file, Bitmap thumbnail, NodeItem parent) {
        super(thumbnail, parent);
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

    public File getFile() {
        return file;
    }

}
