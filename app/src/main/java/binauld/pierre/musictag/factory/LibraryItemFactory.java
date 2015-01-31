package binauld.pierre.musictag.factory;


import android.content.res.Resources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.ChildItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.item.itemable.AudioFile;
import binauld.pierre.musictag.item.itemable.Folder;
import binauld.pierre.musictag.wrapper.FileWrapper;

/**
 * Build a library item from a source file.
 * Allows activity to use item as folder or audio file.
 */
public class LibraryItemFactory {

    private FileWrapper wrapper;
    private BitmapDecoder folderBitmapDecoder;
    private BitmapDecoder defaultArtworkDecoder;
    private FileFilter filter;
    private Resources res;

    public LibraryItemFactory(FileWrapper wrapper, BitmapDecoder defaultArtworkDecoder, BitmapDecoder folderBitmapDecoder, Resources res) {
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
    public LibraryItem build(File file, NodeItem parent) throws IOException {
        if (file.isDirectory()) {
            return buildNodeItem(file, parent);
        } else {
            return buildItem(file, parent);
        }
    }

    public ChildItem buildItem(File file, NodeItem parent) throws IOException {
        AudioFile audioFile = wrapper.build(file);
        BitmapDecoder decoder = audioFile.getBitmapDecoder();
        if(null == decoder) {
            decoder = defaultArtworkDecoder;
        }
        audioFile.setBitmapDecoder(decoder);

        ChildItem audioItem = new ChildItem(audioFile);
        audioItem.setParent(parent);
//        audioItem.setAudioFile(audioFile);
//        audioItem.setBitmapDecoder(decoder);

        return audioItem;
    }

    public NodeItem buildNodeItem(File file, NodeItem parent) {
        Folder folder = new Folder(file, filter, res);
        folder.setBitmapDecoder(folderBitmapDecoder);
        NodeItem node = new NodeItem(folder, parent);
        return node;
    }
}