package binauld.pierre.musictag.item.itemable;

import java.io.File;

import binauld.pierre.musictag.tag.Id3Tag;

public interface AudioFile extends Itemable {

    public Id3Tag getId3Tag();

    public File getFile();
}