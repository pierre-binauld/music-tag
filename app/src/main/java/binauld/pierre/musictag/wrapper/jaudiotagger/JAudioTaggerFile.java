package binauld.pierre.musictag.wrapper.jaudiotagger;


import java.io.File;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.wrapper.AudioFile;

public class JAudioTaggerFile implements AudioFile {

    private BitmapDecoder bitmapDecoder;
    private Id3Tag id3Tag;
    private File file;

    @Override
    public BitmapDecoder getBitmapDecoder() {
        return bitmapDecoder;
    }

    public void setBitmapDecoder(BitmapDecoder bitmapDecoder) {
        this.bitmapDecoder = bitmapDecoder;
    }

    @Override
    public Id3Tag getId3Tag() {
        return id3Tag;
    }

    public void setId3Tag(Id3Tag id3Tag) {
        this.id3Tag = id3Tag;
    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
