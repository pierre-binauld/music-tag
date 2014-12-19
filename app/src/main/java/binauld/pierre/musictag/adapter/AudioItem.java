package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;

/**
 * Represent an audio file in a library.
 */
public class AudioItem extends ChildItem {

    private final AudioFile audio;
    private final Bitmap thumbnail;

    public AudioItem(AudioFile audio, Bitmap thumbnail) {
        this.audio = audio;
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean isSong() {
        return true;
    }

    @Override
    public String getPrimaryInformation() {
        String primary = audio.getTag().getFirst(FieldKey.TITLE);
        if(StringUtils.isBlank(primary)) {
            primary = audio.getFile().getName();
        }
        return primary;
    }

    @Override
    public String getSecondaryInformation() {
        return audio.getTag().getFirst(FieldKey.ARTIST);
    }

    @Override
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public AudioFile getAudio() {
        return audio;
    }
}
