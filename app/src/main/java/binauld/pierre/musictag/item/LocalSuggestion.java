package binauld.pierre.musictag.item;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class LocalSuggestion implements Suggestion {

    private AudioFile audioFile;
    private Tag tags;
    private boolean isSelected;

    public LocalSuggestion(AudioFile audioFile) {
        this.audioFile = audioFile;
        this.tags = this.audioFile.getTag();
    }

    @Override
    public String getTrack() {
        return tags.getFirst(FieldKey.TRACK);
    }

    @Override
    public String getTitle() {
        return tags.getFirst(FieldKey.TITLE);
    }

    @Override
    public String getTAlbum() {
        return tags.getFirst(FieldKey.ALBUM);
    }

    @Override
    public String getArtist() {
        return tags.getFirst(FieldKey.ARTIST);
    }

    @Override
    public String getGenre() {
        return tags.getFirst(FieldKey.GENRE);
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
