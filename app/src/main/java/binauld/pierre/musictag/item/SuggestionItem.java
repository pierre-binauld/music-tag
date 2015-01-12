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

    /**
     * Get the track number.
     * @return The track number.
     */
    public String getTrack() {
        return id3Tag.get(SupportedTag.TRACK);
    }

    /**
     * Get the title.
     * @return The title.
     */
    public String getTitle() {
        return id3Tag.get(SupportedTag.TITLE);
    }

    /**
     * Get the album.
     * @return The album.
     */
    public String getTAlbum() {
        return id3Tag.get(SupportedTag.ALBUM);
    }

    /**
     * Get the artist.
     * @return The artist.
     */
    public String getArtist() {
        return id3Tag.get(SupportedTag.ARTIST);
    }

    /**
     * Get the genre.
     * @return The genre.
     */
    public String getGenre() {
        return id3Tag.get(SupportedTag.GENRE);
    }

    /**
     * Get the year.
     * @return The year.
     */
    public String getYear() {
        return id3Tag.get(SupportedTag.YEAR);
    }

    /**
     * Get the score.
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the score.
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Check if the item is selected.
     * @return true if it does, else false.
     */
    public boolean isSelected() {
        return false;
    }

    /**
     * Set a boolean to check if the item is selected.
     * @param isSelected The boolean.
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Get the Id3Tag.
     * @return The Id3Tag.
     */
    public Id3Tag getTag() {
        return id3Tag;
    }
}
