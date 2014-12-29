package binauld.pierre.musictag.tag;

import android.util.Log;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent a bundle of id3 tag supported by the app.
 */
public class Id3Tag {

    protected HashMap<SupportedTag, String> tags = new HashMap<>();

    public Id3Tag() {
    }

    public Id3Tag(AudioFile audioFile) {
        Tag audioFileTags = audioFile.getTag();
        for(Map.Entry<SupportedTag, FieldKey> entry : SupportedTag.getSupportedTags().entrySet()) {
            tags.put(entry.getKey(), audioFileTags.getFirst(entry.getValue()));
        }
    }

    /**
     * Get the value of a supported tag.
     * @param tag A supported tag.
     * @return The value of the tag.
     */
    public String get(SupportedTag tag){
        return tags.get(tag);
    }

    /**
     * Put the value of a supported tag.
     * @param tag A supported tag.
     * @param value The value of the tag.
     */
    public void put(SupportedTag tag, String value) {
        tags.put(tag, value);
    }

    /**
     * Save the bundle of tag into an audioFile.
     * @param audioFile The audio to save the tag in.
     */
    public void saveInto(AudioFile audioFile) {
        Tag audioFileTag = audioFile.getTag();
        HashMap<SupportedTag, FieldKey> supportedTags = SupportedTag.getSupportedTags();

        for(Map.Entry<SupportedTag, String> entry : tags.entrySet()) {
            try {
                audioFileTag.setField(supportedTags.get(entry.getKey()), entry.getValue());
            } catch (FieldDataInvalidException e) {
                Log.w(this.getClass().toString(), e.getMessage(), e);
            }
        }
    }
}
