package binauld.pierre.musictag.wrapper.musicbrainz;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

/**
 * The QueryBuilder allow to build a string query for MusicBrainz API.
 */
public class QueryBuilder {

    private static HashMap<SupportedTag, String> parameters = new HashMap<>();

    static {
        parameters.put(SupportedTag.ARTIST, "artist");
        parameters.put(SupportedTag.TRACK, "tnum");
        parameters.put(SupportedTag.ALBUM, "release");
        parameters.put(SupportedTag.ALBUM_ARTIST, "artistname");
        parameters.put(SupportedTag.DISC_NO, "position");
        parameters.put(SupportedTag.YEAR, "date");

        //TODO: Have to do a lookup to find more tags
//        parameters.put(SupportedTag.COMPOSER, "");
//        parameters.put(SupportedTag.GROUPING, "");
//        parameters.put(SupportedTag.GENRE, "");
    }

    StringBuilder query = new StringBuilder();

    @Override
    public String toString() {
        return query.toString();
    }

    /**
     * Build a request for a specified Id3Tag.
     * @param id3Tag The specified Id3Tag.
     * @return The request built.
     */
    public String build(Id3Tag id3Tag) {

        for(Map.Entry<SupportedTag, String> tag : id3Tag.entrySet()) {
            this.put(tag.getKey(), tag.getValue());
        }

        return this.toString();
    }

    /**
     * Put into the request the tag and its associated value.
     * @param tag The tag.
     * @param value The tag value.
     */
    public void put(SupportedTag tag, String value) {

        if(SupportedTag.TITLE == tag) {
            insertTitle(value);

        } else if (parameters.containsKey(tag) && StringUtils.isNotBlank(value)) {
            query.append(parameters.get(tag));
            query.append(":");
            query.append(value);
            query.append(" ");
        }
    }

    /**
     * Insert the title in the request.
     * @param title The title of the looked recording up.
     */
    public void insertTitle(String title) {
        if(StringUtils.isNotBlank(title)) {
            query.insert(0, " AND ");
            query.insert(0, title);
        }
    }
}
