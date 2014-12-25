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

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, targetedWidth, targetedHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length, options);
        }
        return bitmap;
    }

    @Override
    public String getKey(int targetedWidth, int targetedHeight) {
        return targetedWidth + "." + targetedHeight + "." + audioFile.getFile().getAbsolutePath();
    }

    /**
     * Calculate th in sample size for decode the bitmap at the right size.
     * @param options Options of the bitmap.
     * @param targetedWidth The targeted width.
     * @param targetedHeight The targeted Height.
     * @return The calculated in sample size.
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int targetedWidth, int targetedHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > targetedHeight || width > targetedWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > targetedHeight
                    && (halfWidth / inSampleSize) > targetedWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
