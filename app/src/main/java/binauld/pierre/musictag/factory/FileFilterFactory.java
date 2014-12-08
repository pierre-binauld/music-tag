package binauld.pierre.musictag.factory;


import org.jaudiotagger.audio.AudioFileFilter;

import java.io.FileFilter;

/**
 * Build a file filter to accept only supported audio files and folders.
 * Usage of factory prevent coupling with Third-Party API
 */
public class FileFilterFactory {

    /**
     * Build a file filter to accept only supported audio files and folders.
     * @return a file filter.
     */
    public FileFilter build() {
        return new AudioFileFilter(true);
    }
}
