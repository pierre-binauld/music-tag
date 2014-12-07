package binauld.pierre.musictag.file;


import org.jaudiotagger.audio.AudioFile;

public class SongItem implements LibraryItem {

    private AudioFile song;

    public SongItem(AudioFile song) {
        this.song = song;
    }

    @Override
    public boolean isSong() {
        return true;
    }
}
