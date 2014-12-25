package binauld.pierre.musictag.item;

import java.io.File;
import java.io.FileFilter;

/**
 * Represent a folder in the library.
 */
public class FolderItem extends NodeItem {

    private File file;
    private File[] fileChildren;
    private String secondaryInformation;

    public FolderItem(File file, FileFilter filter) {
        this(file, filter, null);
    }

    public FolderItem(File file, FileFilter filter, NodeItem parent) {
        super(parent);
        this.file = file;
        this.fileChildren = file.listFiles(filter);
//        this.filter = filter;

        int fileNumber = getLength();
        // TODO: Add local logic
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

    /**
     * Get the number of sub file.
     * @return The number of sub file.
     */
    public int getLength() {
        return file.isDirectory()?file.list().length:0;
    }

    public File[] getFileChildren() {
        return fileChildren;
    }

    @Override
    public int getMaxChildren() {
        return fileChildren.length;
    }
}
