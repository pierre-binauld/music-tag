package binauld.pierre.musictag.helper;


import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;

import java.io.FileFilter;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryItemFactory;

public class LibraryItemFactoryHelper {

    /**
     * Build the library item factory.
     * @param activity The activity context.
     * @return A library item factory.
     */
    public static LibraryItemFactory buildFactory(Activity activity, FileFilter filter) {
        Resources res = activity.getResources();
        BitmapDecoder folderBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.folder);
        return new LibraryItemFactory(folderBitmapDecoder, filter, getThumbnailSize(activity));
    }

    /**
     * Retrieve the thumbnail size from listPreferredItemHeight theme attribute.
     * @return The thumbnail size.
     */
    private static int getThumbnailSize(Activity activity) {

        TypedValue thumbnailSize = new android.util.TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, thumbnailSize, true);
        TypedValue.coerceToString(thumbnailSize.type, thumbnailSize.data);

        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return (int) thumbnailSize.getDimension(metrics);
    }
}
