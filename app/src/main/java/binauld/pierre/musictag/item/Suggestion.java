package binauld.pierre.musictag.item;


public interface Suggestion {
    String getTrack();

    String getTitle();

    String getTAlbum();

    String getArtist();

    String getGenre();

    boolean isSelected();

    void setSelected(boolean selected);
}
