package binauld.pierre.musictag.item.itemable;

import org.apache.commons.lang.StringUtils;

import java.io.File;

import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class AudioFileImpl extends DecoderItemable implements AudioFile {

    private Id3Tag id3Tag;
    private File file;

    private String primaryInformation;
    private String secondaryInformation;

    public AudioFileImpl(File file, Id3Tag id3Tag) {
        this.file = file;
        this.setId3Tag(id3Tag);
    }

    @Override
    public String getPrimaryInformation() {
        return primaryInformation;
    }

    @Override
    public String getSecondaryInformation() {
        return secondaryInformation;
    }

    @Override
    public Id3Tag getId3Tag() {
        return id3Tag;
    }

    public void setId3Tag(Id3Tag id3Tag) {
        this.id3Tag = id3Tag;

        this.primaryInformation = this.id3Tag.get(SupportedTag.TITLE);
        if(StringUtils.isBlank(primaryInformation)) {
            primaryInformation = file.getName();
        }

        this.secondaryInformation = this.id3Tag.get(SupportedTag.ARTIST);
    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}