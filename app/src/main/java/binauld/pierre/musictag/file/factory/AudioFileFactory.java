package binauld.pierre.musictag.file.factory;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public interface AudioFileFactory {

    String getFormat();

    AudioFile build(File file) throws ReadOnlyFileException, TagException, InvalidAudioFrameException, IOException;
}
