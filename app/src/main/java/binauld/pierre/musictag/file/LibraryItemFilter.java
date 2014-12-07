package binauld.pierre.musictag.file;


import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class LibraryItemFilter implements FileFilter {

    private Set<String> supportedAudioFile;

    public LibraryItemFilter(Set<String> supportedAudioFile) {
        this.supportedAudioFile = supportedAudioFile;
    }

    @Override
    public boolean accept(File file) {
        if(file.isDirectory() || isSupportedAudioFile(file)) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isSupportedAudioFile(File file) {
        String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
        if(supportedAudioFile.contains(extension)) {
            return true;
        } else {
            return false;
        }
    }
}
