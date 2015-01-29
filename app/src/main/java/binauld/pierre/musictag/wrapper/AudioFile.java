package binauld.pierre.musictag.wrapper;

import java.io.File;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.tag.Id3Tag;

public interface AudioFile {

    public BitmapDecoder getBitmapDecoder();

    public Id3Tag getId3Tag();

    public File getFile();
}
