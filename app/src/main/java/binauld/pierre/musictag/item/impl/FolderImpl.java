package binauld.pierre.musictag.item.impl;

import android.content.res.Resources;

import java.io.File;
import java.io.FileFilter;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.visitor.ItemVisitor;
import binauld.pierre.musictag.item.Folder;

public class FolderImpl extends DecoderItem implements Folder {

    private File file;
    private File[] fileList;

    private String secondaryInformation;

    public FolderImpl(File file, FileFilter fileFilter, Resources res) {
        this.file = file;
        this.fileList = file.listFiles(fileFilter);

        int fileNumber = fileList.length;
        this.secondaryInformation = fileNumber + " ";
        if (fileNumber < 2) {
            this.secondaryInformation += res.getString(R.string.file);
        } else {
            this.secondaryInformation += res.getString(R.string.files);
        }
    }

    @Override
    public String getPrimaryInformation() {
        return file.getName();
    }

    @Override
    public String getSecondaryInformation() {
        return this.secondaryInformation;
    }

    @Override
    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public File[] getFileList() {
        return fileList;
    }

    @Override
    public int getMaxChildrenCount() {
        if (null != fileList) {
            return fileList.length;
        } else {
            return 0;
        }
    }
}
