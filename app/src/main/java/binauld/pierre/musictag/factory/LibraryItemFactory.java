package binauld.pierre.musictag.factory;


import android.content.res.Resources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.wrapper.AudioFile;
import binauld.pierre.musictag.wrapper.FileWrapper;
import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;

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

    public LibraryItemFactory(BitmapDecoder defaultArtworkDecoder, BitmapDecoder folderBitmapDecoder, FileFilter filter, Resources res) {
        this.wrapper = new JAudioTaggerWrapper();
        this.folderBitmapDecoder = folderBitmapDecoder;
        this.defaultArtworkDecoder = defaultArtworkDecoder;
        this.filter = filter;
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
            return buildFolderItem(file, parent);
        } else {
            return buildAudioItem(file, parent);
        }
    }

    public AudioItem buildAudioItem(File file, NodeItem parent) throws IOException {
        AudioFile audioFile = wrapper.build(file);
        BitmapDecoder decoder = audioFile.getBitmapDecoder();
        if(null == decoder) {
            decoder = defaultArtworkDecoder;
        }

        AudioItem audioItem = new AudioItem();
        audioItem.setParent(parent);
        audioItem.setAudioFile(audioFile);
        //TODO: meh
        audioItem.switchDecoder(decoder);

        return audioItem;
    }

    public FolderItem buildFolderItem(File file, NodeItem parent) {
        FolderItem folder = new FolderItem(file, filter, parent, res);
        folder.switchDecoder(folderBitmapDecoder);
        return folder;
    }

//    @Deprecated
//    private void put(AudioItem item, File file) throws IOException {
//        if (!file.isDirectory()) {
//            try {
//                AudioFile audio = AudioFileIO.read(file);
//                item.setAudioFile(audio);
//                if(null != audio.getTag().getFirstArtwork()) {
//                    item.switchDecoder(new AudioFileBitmapDecoder(audio));
//                } else {
//                    item.switchDecoder(defaultArtworkDecoder);
//                }
//
//            } catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
//                throw new IOException(e);
//            }
//        } else {
//            throw new IOException(file.getAbsolutePath() + " is not a directory.");
//        }
//    }
}
