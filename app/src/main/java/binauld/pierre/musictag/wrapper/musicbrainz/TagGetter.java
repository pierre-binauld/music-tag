package binauld.pierre.musictag.wrapper.musicbrainz;

import org.musicbrainz.modelWs2.Entity.RecordingWs2;
import org.musicbrainz.modelWs2.Entity.ReleaseWs2;
import org.musicbrainz.modelWs2.MediumListWs2;
import org.musicbrainz.modelWs2.MediumWs2;

import java.util.HashMap;
import java.util.List;

import binauld.pierre.musictag.tag.SupportedTag;

/**
 * Allow to get a specific tag from a RecordingWs2 provide by MusicBrainz java API.
 */
public abstract class TagGetter {

    /**
     * Get a specific tag from a RecordingWs2.
     * @param recordingWs2 A RecordingWs2 provide by MusicBrainz java API.
     * @return The value of the tag looked up.
     */
    public abstract String get(RecordingWs2 recordingWs2);

    /**
     * Help children class to get the release of a RecordingWs2.
     * @param recordingWs2 A RecordingWs2 provide by MusicBrainz java API.
     * @return The release.
     */
    protected ReleaseWs2 getRelease(RecordingWs2 recordingWs2) {
        List<ReleaseWs2> releases = recordingWs2.getReleases();
        if (releases.isEmpty()) {
            return null;
        }

        return releases.get(0);
    }

    public static HashMap<SupportedTag, TagGetter> getters = new HashMap<>();

    static {
        getters.put(SupportedTag.TITLE, new TitleGetter());
        getters.put(SupportedTag.ARTIST, new ArtistGetter());
        getters.put(SupportedTag.ALBUM, new AlbumGetter());
        getters.put(SupportedTag.YEAR, new YearGetter());
        getters.put(SupportedTag.TRACK, new TrackGetter());
//        getters.put(SupportedTag.DISC_NO, new DiscNoGetter());
//        getters.put(SupportedTag.COMPOSER, new ComposerGetter());

        //TODO: Have to do a lookup to find more tags
        getters.put(SupportedTag.GROUPING, new GroupingGetter());
    }

    /**
     * Getter class to get the title tag.
     */
    static class TitleGetter extends TagGetter {
        @Override
        public String get(RecordingWs2 recordingWs2) {
            return recordingWs2.getTitle();
        }
    }

    /**
     * Getter class to get the artist tag.
     */
    static class ArtistGetter extends TagGetter {
        @Override
        public String get(RecordingWs2 recordingWs2) {
            return recordingWs2.getArtistCreditString();
        }
    }

    /**
     * Getter class to get the year tag.
     */
    static class YearGetter extends TagGetter {
        @Override
        public String get(RecordingWs2 recordingWs2) {
            ReleaseWs2 release = getRelease(recordingWs2);
            if (null == release) {
                return "";
            }

            return release.getYear();
        }
    }

    /**
     * Getter class to get the album tag.
     */
    static class AlbumGetter extends TagGetter {
        @Override
        public String get(RecordingWs2 recordingWs2) {
            ReleaseWs2 release = getRelease(recordingWs2);
            if (null == release) {
                return "";
            }

            return release.getTitle();
        }
    }

    /**
     * Getter class to get the track tag.
     */
    static class TrackGetter extends TagGetter {
        @Override
        public String get(RecordingWs2 recordingWs2) {

            ReleaseWs2 release = getRelease(recordingWs2);
            if (null == release) {
                return "";
            }

            MediumListWs2 mediumList = release.getMediumList();
            if (null == mediumList) {
                return "";
            }

            List<MediumWs2> media = mediumList.getMedia();
            if (null == media) {
                return "";
            }

            MediumWs2 medium = media.get(0);
            if (null == medium) {
                return "";
            }

            return String.valueOf(medium.getPosition());
        }
    }

    /**
     * Getter class to get the composer tag.
     */
    static class GroupingGetter extends TagGetter {
        @Override
        public String get(RecordingWs2 recordingWs2) {

            ReleaseWs2 release = getRelease(recordingWs2);
            if (null == release) {
                return "";
            }

            return release.getLabelInfoString();
        }
    }
}
