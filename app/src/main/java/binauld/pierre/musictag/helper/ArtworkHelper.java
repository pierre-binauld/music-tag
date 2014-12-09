package binauld.pierre.musictag.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * Help to build artwork from audio file.
 */
public class ArtworkHelper {

    /**
     * Build an artwork Bitmap from a source audio file.
     * @param song The source audio file.
     * @return The artwork as Bitmap.
     */
    public static Bitmap buildBitmap(AudioFile song) {
        Artwork artwork = song.getTag().getFirstArtwork();
        if(null == artwork) {
            return null;
        } else {
            byte[] data = artwork.getBinaryData();
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
    }
}
