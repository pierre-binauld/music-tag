package binauld.pierre.musictag.item;


import org.apache.commons.lang.StringUtils;

import binauld.pierre.musictag.tag.SupportedTag;
import binauld.pierre.musictag.wrapper.AudioFile;

/**
 * Represent an audio file in a library.
 */
public class AudioItem extends ChildItem {

    private AudioFile audioFile;
    private String primaryInformation;
    private String secondaryInformation;

    @Override
    public boolean isAudioItem() {
        return true;
    }

    @Override
    public String getPrimaryInformation() {
        return primaryInformation;
    }

    @Override
    public String getSecondaryInformation() {
        return secondaryInformation;
    }

    /**
     * Get the AudioFile.
     * @return The audio file.
     */
    public AudioFile getAudioFile() {
        return audioFile;
    }

    /**
     * Set the AudioFile.
     * @param audio The audio file.
     */
    public void setAudioFile(AudioFile audio) {
        this.audioFile = audio;

        this.primaryInformation = audioFile.getId3Tag().get(SupportedTag.TITLE);
        if(StringUtils.isBlank(primaryInformation)) {
            primaryInformation = audioFile.getFile().getName();
        }

        this.secondaryInformation = audio.getId3Tag().get(SupportedTag.ARTIST);
    }

}
