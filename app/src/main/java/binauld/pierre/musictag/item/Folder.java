package binauld.pierre.musictag.item;


import java.io.File;

public interface Folder extends Item {

    File getFile();

    File[] getFileList();

    int getMaxChildrenCount();
}
