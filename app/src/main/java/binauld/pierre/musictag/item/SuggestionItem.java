package binauld.pierre.musictag.item;


import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

/**
 * Suggestion which is displayed as item in the suggestion list.
 */
public class SuggestionItem {

    private Id3Tag id3Tags;
    private boolean isSelected;

    public SuggestionItem(Id3Tag id3Tags) {
        this.id3Tags = id3Tags;
    }

    public String getTrack() {
        return id3Tags.get(SupportedTag.TRACK);
    }

    public String getTitle() {
        return id3Tags.get(SupportedTag.TITLE);
    }

    public String getTAlbum() {
        return id3Tags.get(SupportedTag.ALBUM);
    }

    public String getArtist() {
        return id3Tags.get(SupportedTag.ARTIST);
    }

    public String getGenre() {
        return id3Tags.get(SupportedTag.GENRE);
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Id3Tag getTags() {
        return id3Tags;
    }
}
