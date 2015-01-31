package binauld.pierre.musictag.item.itemable;

import android.content.res.Resources;

import java.io.File;
import java.io.FileFilter;

import binauld.pierre.musictag.R;

public class Folder extends DecoderItemable {

    private File file;
    private File[] fileList;

    private String secondaryInformation;

    public Folder(File file, FileFilter fileFilter, Resources res) {
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


    public File getFile() {
        return file;
    }

    public File[] getFileList() {
        return fileList;
    }

    public int getMaxChildrenCount() {
        if (null != fileList) {
            return fileList.length;
        } else {
            return 0;
        }
    }
}
