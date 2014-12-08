package binauld.pierre.musictag.adapter;


import android.graphics.Bitmap;

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;

import binauld.pierre.musictag.helper.ArtworkHelper;

/**
 * Represent an audio file in a library.
 */
public class AudioItem implements LibraryItem {

    private AudioFile song;
    private Bitmap thumbnail;

    public AudioItem(AudioFile song) {
        this.song = song;
        this.thumbnail = ArtworkHelper.buildBitmap(this.song);
    }

    @Override
    public boolean isSong() {
        return true;
    }

    @Override
    public String getPrimaryInformation() {
        String primary = song.getTag().getFirst(FieldKey.TITLE);
        if(StringUtils.isBlank(primary)) {
            primary = song.getFile().getName();
        }
        return primary;
    }

    @Override
    public String getSecondaryInformation() {
        return song.getTag().getFirst(FieldKey.ARTIST);
    }

    @Override
    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
