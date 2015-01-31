package binauld.pierre.musictag.wrapper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import binauld.pierre.musictag.item.itemable.AudioFile;

public interface FileWrapper {

    public AudioFile build(File file) throws IOException;

    public void save(AudioFile audioFile) throws IOException;

    /**
     * Get a file filter witch accept only supported audio files and folders.
     * @return a file filter.
     */
    public FileFilter getFileFilter();
}
