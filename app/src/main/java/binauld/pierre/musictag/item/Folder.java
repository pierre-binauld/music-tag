package binauld.pierre.musictag.item;


import java.io.File;

public interface Folder {

    File getFile();

    File[] getFileList();

    int getMaxChildrenCount();
}
