package binauld.pierre.musictag.factory;


import android.content.res.Resources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.impl.FolderImpl;
import binauld.pierre.musictag.wrapper.FileWrapper;

/**
 * Build a library item from a source file.
 * Allows activity to use item as folder or audio file.
 */
public class LibraryComponentFactory {

    private FileWrapper wrapper;
    private BitmapDecoder folderBitmapDecoder;
    private BitmapDecoder defaultArtworkDecoder;
    private FileFilter filter;
    private Resources res;

    public LibraryComponentFactory(FileWrapper wrapper, BitmapDecoder defaultArtworkDecoder, BitmapDecoder folderBitmapDecoder, Resources res) {
        this.wrapper = wrapper;
        this.folderBitmapDecoder = folderBitmapDecoder;
        this.defaultArtworkDecoder = defaultArtworkDecoder;
        this.filter = wrapper.getFileFilter();
        this.res = res;
    }

    /**
     * Build a library item from a source file.
     *
     * @param file   The source file. It has to be either a folder or a supported audio file.
     * @param parent The parent of the item
     * @return A library item
     * @throws IOException Exception is thrown when a problem occurs with the reading, tags or the format.
     */
    public LibraryComponent build(File file, LibraryComposite parent) throws IOException {
        if (file.isDirectory()) {
            return buildComposite(file, parent);
        } else {
            return buildLeaf(file, parent);
        }
    }

    public LibraryLeaf buildLeaf(File file, LibraryComposite parent) throws IOException {
        AudioFile audioFile = wrapper.build(file);
        BitmapDecoder decoder = audioFile.getBitmapDecoder();
        if(null == decoder) {
            decoder = defaultArtworkDecoder;
        }
        audioFile.setBitmapDecoder(decoder);

        LibraryLeaf audioItem = new LibraryLeaf(audioFile);
        audioItem.setParent(parent);
//        audioItem.setAudioFile(audioFile);
//        audioItem.setBitmapDecoder(decoder);

        return audioItem;
    }

    public LibraryComposite buildComposite(File file, LibraryComposite parent) {
        FolderImpl folder = new FolderImpl(file, filter, res);
        folder.setBitmapDecoder(folderBitmapDecoder);
        LibraryComposite composite = new LibraryComposite(folder, parent);
        return composite;
    }
}