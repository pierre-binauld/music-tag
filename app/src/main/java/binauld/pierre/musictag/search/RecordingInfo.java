package binauld.pierre.musictag.search;

import java.util.Comparator;

/**
 * Created by mathieu on 19/12/2014.
 */
public class RecordingInfo {

    protected String id;
    protected int score;
    protected String title;
    protected String artist;

    public static Comparator<RecordingInfo> comparator = new Comparator<RecordingInfo>() {
        @Override
        public int compare(RecordingInfo recordingInfo, RecordingInfo recordingInfo2) {
            return recordingInfo2.getScore()-recordingInfo.getScore();
        }
    };

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof RecordingInfo))
            return false;

        final RecordingInfo ri = (RecordingInfo) other;

        return getId().equals(ri.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public RecordingInfo(String id, int score) {
        this.id = id;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
