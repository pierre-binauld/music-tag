package binauld.pierre.musictag.file.factory;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class Mp3FileFactory implements AudioFileFactory {

    public static final String FORMAT = "mp3";

    @Override
    public String getFormat() {
        return FORMAT;
    }

    @Override
    public AudioFile build(File file) throws ReadOnlyFileException, TagException, InvalidAudioFrameException, IOException {
        return new MP3File(file);
    }
}
