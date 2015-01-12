package binauld.pierre.musictag.wrapper;

import android.util.Log;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class JAudioTaggerWrapper {

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

    public Id3Tag build(AudioFile audioFile) {
        Tag audioFileTags = audioFile.getTag();
        Id3Tag id3Tag = new Id3Tag();

        for(Map.Entry<SupportedTag, FieldKey> entry : fieldKeys.entrySet()) {
            id3Tag.put(entry.getKey(), audioFileTags.getFirst(entry.getValue()));
        }

        return id3Tag;
    }


    /**
     * Save the bundle of tag into an audioFile.
     * @param audioFile The audio to save the tag in.
     */
    public void save(Id3Tag tag, AudioFile audioFile) throws IOException {
        Tag audioFileTag = audioFile.getTag();

        for(SupportedTag supportedTag : tag.keySet()) {
            try {
                TagSetter tagSetter = setters.get(supportedTag);
                tagSetter.setTagField(audioFileTag, fieldKeys.get(supportedTag), tag.get(supportedTag));
            } catch (FieldDataInvalidException e) {
                Log.w(this.getClass().toString(), e.getMessage(), e);
            }
        }

        try {
            AudioFileIO.write(audioFile);
        } catch (CannotWriteException e) {
            throw new IOException(e);
        }
    }
}
