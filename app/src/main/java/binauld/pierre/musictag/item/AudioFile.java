package binauld.pierre.musictag.item;

import java.io.File;

import binauld.pierre.musictag.tag.Id3Tag;

public interface AudioFile extends Item {

    public Id3Tag getId3Tag();

    public void setId3Tag(Id3Tag id3Tag);

    public File getFile();
}