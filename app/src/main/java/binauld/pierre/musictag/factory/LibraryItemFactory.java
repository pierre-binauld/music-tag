package binauld.pierre.musictag.factory;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

import binauld.pierre.musictag.adapter.AudioItem;
import binauld.pierre.musictag.adapter.FolderItem;
import binauld.pierre.musictag.adapter.LibraryItem;
import binauld.pierre.musictag.adapter.NodeItem;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Build a library item from a source file.
 * Allows activity to use item as folder or audio file.
 */
public class LibraryItemFactory {

    private ThumbnailService thumbnailService;

    public LibraryItemFactory(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    /**
     * Build a library item from a source file.
     * @param file The source file. It has to be either a folder or a supported audio file.
     * @param parent The parent of the item
     * @return A library item
     * @throws IOException Exception is thrown when a problem occurs with the reading, tags or the format.
     */
    public LibraryItem build(File file, NodeItem parent) throws IOException {
        if(file.isDirectory()) {
            return new FolderItem(file, thumbnailService.getFolder(), parent);
        } else {
            try {
                AudioFile audio = AudioFileIO.read(file);
                AudioItem audioItem = new AudioItem(audio, thumbnailService.getArtwork(audio));
                audioItem.setParent(parent);
                return audioItem;
            } catch (CannotReadException e) {
                throw new IOException(e);
            } catch (TagException e) {
                throw new IOException(e);
            } catch (ReadOnlyFileException e) {
                throw new IOException(e);
            } catch (InvalidAudioFrameException e) {
                throw new IOException(e);
            }
        }
    }
}
