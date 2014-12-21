package binauld.pierre.musictag.decoder;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.datatype.Artwork;

public class AudioFileBitmapDecoder implements BitmapDecoder {

    private AudioFile audioFile;

    private int targetedWidth;
    private int targetedHeight;

    public AudioFileBitmapDecoder(AudioFile audioFile, int targetedWidth, int targetedHeight) {
        this.audioFile = audioFile;
        this.targetedHeight = targetedHeight;
        this.targetedWidth = targetedWidth;
    }

    @Override
    public Bitmap decode() {
        Bitmap bitmap = null;
//        Artwork artwork = audioFile.getTag().getFirstArtwork();
//        if(null != artwork) {
//            byte[] artworkData = artwork.getBinaryData();
//            bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length, options);
//        }
//        return bitmap;

        Artwork artwork = audioFile.getTag().getFirstArtwork();
        if(null != artwork) {
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
    public String getId() {
        return audioFile.getFile().getAbsolutePath();
    }
}
