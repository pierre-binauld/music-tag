package binauld.pierre.musictag.helper;


import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.service.ThumbnailService;

public class AdapterHelper {

    /**
     * Build the adapter used to adapt library item for the list view.
     * @param thumbnailService The thumbnail service for retrieve thumbnail.
     * @return The LibraryAdapter built.
     */
    public static LibraryItemAdapter buildAdapter(ThumbnailService thumbnailService) {
        return new LibraryItemAdapter(thumbnailService);
        //TODO: Useless ?
    }
}
