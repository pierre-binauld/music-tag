package binauld.pierre.musictag.file;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.datatype.Artwork;

public class SongItem implements LibraryItem {

    private AudioFile song;

    public SongItem(AudioFile song) {
        this.song = song;
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
        Artwork artwork = song.getTag().getFirstArtwork();
        if(null == artwork) {
            return null;
        } else {
            byte[] data = artwork.getBinaryData();
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
    }

    public AudioFile getSong() {
        return song;
    }

    public void setSong(AudioFile song) {
        this.song = song;
    }
}
