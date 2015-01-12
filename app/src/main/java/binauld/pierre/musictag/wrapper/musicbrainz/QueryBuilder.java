package binauld.pierre.musictag.wrapper.musicbrainz;


import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

import binauld.pierre.musictag.tag.SupportedTag;

public class QueryBuilder {

    private static HashMap<SupportedTag, String> parameters = new HashMap<>();

    static {
        parameters.put(SupportedTag.ARTIST, "artist");
        parameters.put(SupportedTag.TRACK, "tnum");
        parameters.put(SupportedTag.ALBUM, "release");
//        parameters.put(SupportedTag.ALBUM_ARTIST, "");
//        parameters.put(SupportedTag.COMPOSER, "");
//        parameters.put(SupportedTag.GROUPING, "");
//        parameters.put(SupportedTag.GENRE, "");
//        parameters.put(SupportedTag.DISC_NO, "");
//        parameters.put(SupportedTag.YEAR, "");
    }

    StringBuilder query = new StringBuilder();

    @Override
    public String toString() {
        return query.toString();
    }

    public void append(SupportedTag tag, String value) {

        if(SupportedTag.TITLE == tag) {
            appendTitle(value);

        } else if (parameters.containsKey(tag) && StringUtils.isNotBlank(value)) {
            query.append(parameters.get(tag));
            query.append(":");
            query.append(value);
            query.append(" ");
        }
    }

    public void appendTitle(String value) {
        if(StringUtils.isNotBlank(value)) {
            query.insert(0, " AND ");
            query.insert(0, value);
        }
    }
}
