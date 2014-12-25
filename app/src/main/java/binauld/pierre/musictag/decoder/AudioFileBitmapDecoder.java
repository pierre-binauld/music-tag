package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jaudiotagger.audio.AudioFile;
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
        if (null != artwork) {
            byte[] artworkData = artwork.getBinaryData();
            bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length);
        }
        return bitmap;
    }

    @Override
    public Bitmap decode(int targetedWidth, int targetedHeight) {
        Bitmap bitmap = null;

        Artwork artwork = audioFile.getTag().getFirstArtwork();
        if (null != artwork) {
            byte[] artworkData = artwork.getBinaryData();

            // Get the dimensions of the bitmap
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length, options);

            int sourceWidth = options.outWidth;
            int sourceHeight = options.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(sourceWidth / targetedWidth, sourceHeight / targetedHeight);

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;

            bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length, options);
        }

        return bitmap;
    }

    @Override
    public String getKey(int targetedWidth, int targetedHeight) {
        return targetedWidth + "." + targetedHeight + "." + audioFile.getFile().getAbsolutePath();
    }
}
