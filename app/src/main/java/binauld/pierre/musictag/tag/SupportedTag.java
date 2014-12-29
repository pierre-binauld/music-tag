package binauld.pierre.musictag.tag;


import org.jaudiotagger.tag.FieldKey;

import java.util.HashMap;

/**
 * List all the supported tags by the app.
 */
public enum SupportedTag {
    TITLE,
    ARTIST,
    ALBUM,
    YEAR,
    DISC_NO,
    TRACK,
    ALBUM_ARTIST,
    COMPOSER,
    GROUPING,
    GENRE;

    private static final HashMap<SupportedTag, FieldKey> supportedTags;

    /**
     * Get a the map between supported tag and third-api tag.
     * @return The map.
     */
    public static HashMap<SupportedTag, FieldKey> getSupportedTags() {
        return supportedTags;
    }

    /**
     * Initialize the map of supported tags.
     */
    static {
        supportedTags = new HashMap<>();
        supportedTags.put(TITLE, FieldKey.TITLE);
        supportedTags.put(ARTIST, FieldKey.ARTIST);
        supportedTags.put(ALBUM, FieldKey.ALBUM);
        supportedTags.put(YEAR, FieldKey.YEAR);
        supportedTags.put(DISC_NO, FieldKey.DISC_NO);
        supportedTags.put(TRACK, FieldKey.TRACK);
        supportedTags.put(ALBUM_ARTIST, FieldKey.ALBUM_ARTIST);
        supportedTags.put(COMPOSER, FieldKey.COMPOSER);
        supportedTags.put(GROUPING, FieldKey.GROUPING);
        supportedTags.put(GENRE, FieldKey.GENRE);
    }
}
