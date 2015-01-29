package binauld.pierre.musictag.helper;


import android.content.res.Resources;

import java.io.FileFilter;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.wrapper.FileWrapper;

public class LibraryItemFactoryHelper {

    /**
     * Build the library item factory.
     * @param res The activity resources.
     * @param defaultArtworkBitmapDecoder The default artwork bitmap decoder.
     * @return A library item factory.
     */
    public static LibraryItemFactory buildFactory(Resources res, FileWrapper wrapper, BitmapDecoder defaultArtworkBitmapDecoder) {
        BitmapDecoder folderBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.folder);
        return new LibraryItemFactory(wrapper, defaultArtworkBitmapDecoder, folderBitmapDecoder, res);
    }
}
