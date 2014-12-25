package binauld.pierre.musictag.helper;


import android.content.res.Resources;

import java.io.FileFilter;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryItemFactory;

public class LibraryItemFactoryHelper {

    /**
     * Build the library item factory.
     * @param res The activity resources.
     * @return A library item factory.
     */
    public static LibraryItemFactory buildFactory(Resources res, FileFilter filter) {
        BitmapDecoder folderBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.folder);
        return new LibraryItemFactory(folderBitmapDecoder, filter);
    }
}
