package binauld.pierre.musictag.item;


import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;

/**
 * Represent an audio file in a library.
 */
public class AudioItem extends ChildItem {

    private AudioFile audio;

    @Override
    public boolean isAudioItem() {
        return true;
    }

    @Override
    public String getPrimaryInformation() {
        String primary = audio.getTag().getFirst(FieldKey.TITLE);
        if(StringUtils.isBlank(primary)) {
            primary = audio.getFile().getName();
        }
        return primary;
    }

    @Override
    public String getSecondaryInformation() {
        return audio.getTag().getFirst(FieldKey.ARTIST);
    }

    /**
     * Get the AudioFile.
     * @return The audio file.
     */
    public AudioFile getAudioFile() {
        return audio;
    }

    /**
     * Set the AudioFile.
     * @param audio The audio file.
     */
    public void setAudioFile(AudioFile audio) {
        this.audio = audio;
    }

}
