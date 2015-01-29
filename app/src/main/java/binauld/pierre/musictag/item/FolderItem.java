package binauld.pierre.musictag.item;

import android.content.res.Resources;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;

/**
 * Represent a folder in the library.
 */
public class FolderItem extends NodeItem {

    private File file;
    private File[] fileList;
    private String secondaryInformation;
    private FileFilter filter;
    private List<FolderItem> folderItems;

    public FolderItem(File file, FileFilter filter, Resources res) {
        this(file, filter, null, res);
    }

    public FolderItem(File file, FileFilter filter, NodeItem parent, Resources res) {
        //TODO: Use AudioFile logic
        super(parent);
        this.file = file;
        this.filter = filter;
        this.fileList = file.listFiles(filter);
        this.folderItems = new ArrayList<>();

        int fileNumber = getLength();
        this.secondaryInformation = fileNumber + " ";
        if(fileNumber < 2) {
            this.secondaryInformation  += res.getString(R.string.file);
        } else {
            this.secondaryInformation  += res.getString(R.string.files);
        }
    }

    //TODO: Check on emulator


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
     * Get the filter.
     * @return The filter.
     */
    public FileFilter getFilter() {
        return filter;
    }

    /**
     * Get the number of sub file.
     * @return The number of sub file.
     */
    public int getLength() {
        return file.isDirectory()?file.list().length:0;
    }

    /**
     * Get the files containing by the folder of this item.
     * @return A list of File.
     */
    public File[] getFileList() {
        return fileList;
    }

    @Override
    public int getMaxChildren() {
        if(null != fileList) {
            return fileList.length;
        } else {
            return 0;
        }
    }

    public List<FolderItem> getFolderItems() {
        return folderItems;
    }
}
