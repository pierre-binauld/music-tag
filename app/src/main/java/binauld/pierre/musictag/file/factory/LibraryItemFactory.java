package binauld.pierre.musictag.file.factory;


import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import binauld.pierre.musictag.exception.UnsupportedFileException;
import binauld.pierre.musictag.file.LibraryItem;
import binauld.pierre.musictag.file.NodeItem;
import binauld.pierre.musictag.file.SongItem;

public class LibraryItemFactory {

    private Map<String, AudioFileFactory> factories = new HashMap<String, AudioFileFactory>();

    public LibraryItem build(File file) throws UnsupportedFileException, ReadOnlyFileException, TagException, InvalidAudioFrameException, IOException {
        if(file.isDirectory()) {
            return new NodeItem(file);
        } else {
            String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();

            AudioFileFactory factory = factories.get(extension);

            if(null == factory) {
                throw new UnsupportedFileException(file);
            } else {
                return new SongItem(factory.build(file));
            }
        }
    }

    public void put(AudioFileFactory factory) {
        factories.put(factory.getFormat().toLowerCase(), factory);
    }

    public Set<String> getSupportedAudioFiles() {
        return factories.keySet();
    }
}
