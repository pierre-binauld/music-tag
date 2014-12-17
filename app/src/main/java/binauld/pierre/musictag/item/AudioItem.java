package binauld.pierre.musictag.item;


import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;

import binauld.pierre.musictag.decoder.AudioFileBitmapDecoder;

/**
 * Represent an audio file in a library.
 */
public class AudioItem extends ChildItem {

    private final AudioFile audio;

    public AudioItem(AudioFile audio) {
        this.audio = audio;
        this.decoder = new AudioFileBitmapDecoder(this.audio);
    }

    @Override
    public boolean getAudio() {
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


//    @Override
//    public Bitmap getThumbnail() {
//        return thumbnail;
//    }
}
