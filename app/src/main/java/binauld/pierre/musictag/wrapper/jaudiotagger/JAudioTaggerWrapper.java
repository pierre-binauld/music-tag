package binauld.pierre.musictag.wrapper.jaudiotagger;

import android.util.Log;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import binauld.pierre.musictag.decoder.AudioFileBitmapDecoder;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;
import binauld.pierre.musictag.wrapper.AudioFile;
import binauld.pierre.musictag.wrapper.FileWrapper;

/**
 * Wrap the JAudioTagger API for a better implementation in the app.
 */
public class JAudioTaggerWrapper implements FileWrapper {

    protected static final HashMap<SupportedTag, FieldKey> fieldKeys;
    protected static final HashMap<SupportedTag, TagSetter> setters;

    /**
     * Initialize the map of supported tags.
     */
    static {
        TagSetter numericTagSetter = new NumericTagSetter();
        TagSetter stringTagSetter = new StringTagSetter();

        fieldKeys = new HashMap<>();
        setters = new HashMap<>();

        fieldKeys.put(SupportedTag.TITLE, FieldKey.TITLE);
        setters.put(SupportedTag.TITLE, stringTagSetter);

        fieldKeys.put(SupportedTag.ARTIST, FieldKey.ARTIST);
        setters.put(SupportedTag.ARTIST, stringTagSetter);

        fieldKeys.put(SupportedTag.ALBUM, FieldKey.ALBUM);
        setters.put(SupportedTag.ALBUM, stringTagSetter);

        fieldKeys.put(SupportedTag.YEAR, FieldKey.YEAR);
        setters.put(SupportedTag.YEAR, numericTagSetter);

        fieldKeys.put(SupportedTag.DISC_NO, FieldKey.DISC_NO);
        setters.put(SupportedTag.DISC_NO, numericTagSetter);

        fieldKeys.put(SupportedTag.TRACK, FieldKey.TRACK);
        setters.put(SupportedTag.TRACK, numericTagSetter);

        fieldKeys.put(SupportedTag.ALBUM_ARTIST, FieldKey.ALBUM_ARTIST);
        setters.put(SupportedTag.ALBUM_ARTIST, stringTagSetter);

        fieldKeys.put(SupportedTag.COMPOSER, FieldKey.COMPOSER);
        setters.put(SupportedTag.COMPOSER, stringTagSetter);

        fieldKeys.put(SupportedTag.GROUPING, FieldKey.GROUPING);
        setters.put(SupportedTag.GROUPING, stringTagSetter);

        fieldKeys.put(SupportedTag.GENRE, FieldKey.GENRE);
        setters.put(SupportedTag.GENRE, stringTagSetter);
    }

    @Override
    public AudioFile build(File file) throws IOException {
        org.jaudiotagger.audio.AudioFile jAudioTaggerFile = buildAudioFile(file);
        Tag jAudioTaggerTags = jAudioTaggerFile.getTag();
        Id3Tag id3Tag = new Id3Tag();

        for (Map.Entry<SupportedTag, FieldKey> entry : fieldKeys.entrySet()) {
            id3Tag.put(entry.getKey(), jAudioTaggerTags.getFirst(entry.getValue()));
        }

        JAudioTaggerFile result = new JAudioTaggerFile();
        result.setId3Tag(id3Tag);
        result.setFile(file);
        result.setBitmapDecoder(new AudioFileBitmapDecoder(jAudioTaggerFile));
        return result;
    }

    private org.jaudiotagger.audio.AudioFile buildAudioFile(File file) throws IOException {
        if (!file.isDirectory()) {
            try {
                return AudioFileIO.read(file);
            } catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                throw new IOException(e);
            }
        } else {
            throw new IOException("Can not build an AudioFile: " + file.getAbsolutePath() + " is a directory.");
        }
    }


    /**
     * Save the bundle of tag into an audioFile.
     *
     * @param audioFile The audio file to save the tag in.
     */
    @Override
    public void save(binauld.pierre.musictag.wrapper.AudioFile audioFile) throws IOException {

        try {
            org.jaudiotagger.audio.AudioFile jAudioTaggerFile = AudioFileIO.read(audioFile.getFile());
            Tag jAudioTaggerTag = jAudioTaggerFile.getTag();
            Id3Tag id3Tag = audioFile.getId3Tag();

            for (SupportedTag supportedTag : id3Tag.keySet()) {
                try {
                    TagSetter tagSetter = setters.get(supportedTag);
                    tagSetter.setTagField(jAudioTaggerTag, fieldKeys.get(supportedTag), id3Tag.get(supportedTag));
                } catch (FieldDataInvalidException e) {
                    Log.w(this.getClass().toString(), e.getMessage(), e);
                }
            }

            AudioFileIO.write(jAudioTaggerFile);
        } catch (CannotWriteException | TagException | ReadOnlyFileException | CannotReadException | InvalidAudioFrameException e) {
            throw new IOException(e);
        }
    }
}
