package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.commons.lang.ObjectUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

public class AudioFileBitmapDecoder implements BitmapDecoder {

    private AudioFile audioFile;

    public AudioFileBitmapDecoder(AudioFile audioFile) {
        this.audioFile = audioFile;
    }

    @Override
    public Bitmap decode() {
        Bitmap bitmap = null;
        Artwork artwork = audioFile.getTag().getFirstArtwork();
        if(null != artwork) {
            byte[] artworkData = artwork.getBinaryData();
            bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length);
        }
        return bitmap;

    }

    @Override
    public String getKey() {
        return audioFile.getFile().getAbsolutePath();
    }
}
