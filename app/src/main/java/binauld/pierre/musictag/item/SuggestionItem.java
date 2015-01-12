package binauld.pierre.musictag.item;


import java.util.Comparator;

import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

/**
 * Suggestion which is displayed as item in the suggestion list.
 */
public class SuggestionItem {

    public static Comparator<SuggestionItem> comparator = new Comparator<SuggestionItem>() {
        @Override
        public int compare(SuggestionItem item1, SuggestionItem item2) {
            return item2.getScore() - item1.getScore();
        }
    };

    private Id3Tag id3Tag;
    private int score;
    private boolean isSelected;

    public SuggestionItem(Id3Tag id3Tag, int score) {
        this.id3Tag = id3Tag;
        this.score = score;
    }

    public String getTrack() {
        return id3Tag.get(SupportedTag.TRACK);
    }

    public String getTitle() {
        return id3Tag.get(SupportedTag.TITLE);
    }

    public String getTAlbum() {
        return id3Tag.get(SupportedTag.ALBUM);
    }

    public String getArtist() {
        return id3Tag.get(SupportedTag.ARTIST);
    }

    public String getGenre() {
        return id3Tag.get(SupportedTag.GENRE);
    }

    public String getYear() {
        return id3Tag.get(SupportedTag.YEAR);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Id3Tag getTags() {
        return id3Tag;
    }
}
