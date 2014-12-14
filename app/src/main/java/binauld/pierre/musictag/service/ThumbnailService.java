package binauld.pierre.musictag.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * Help to build artwork from audio file.
 */
public class ThumbnailService {

    private Bitmap defaultArtwork;
    private Bitmap folder;

    //TODO: may be we can make one method getThumbnail which take the item as parameter ?
    public ThumbnailService(Context context, int defaultArtworkRes, int folderRes) {
        this.defaultArtwork = BitmapFactory.decodeResource(context.getResources(), defaultArtworkRes);
        this.folder = BitmapFactory.decodeResource(context.getResources(), folderRes);
    }

    /**
     * Build an artwork Bitmap from a source audio file.
     * If the source file does not contains an Artwork, then return an defined image.
     *
     * @param song The source audio file.
     * @return The artwork as Bitmap.
     */
    public Bitmap getArtwork(AudioFile song) {
        Artwork artworkTag = song.getTag().getFirstArtwork();
        if (null == artworkTag) {
            return defaultArtwork;
        }

        byte[] artworkData = artworkTag.getBinaryData();
        Bitmap artwork = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length);
        if (null == artwork) {
            return defaultArtwork;
        }

        return artwork;
    }

    /**
     * Get the folder thumbnail.
     * @return The folder thumbnail.
     */
    public Bitmap getFolder() {
        return folder;
    }
}
