package binauld.pierre.musictag.io;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.wrapper.AudioFile;
import binauld.pierre.musictag.wrapper.FileWrapper;

public class TagSaver extends AsyncTask<LibraryItem, Void, Void> {

    private MultipleId3Tag multipleId3Tag;
    private FileWrapper fileWrapper;
    private Callback callback;

    public TagSaver(MultipleId3Tag multipleId3Tag, FileWrapper fileWrapper, Callback callback) {
        this.multipleId3Tag = multipleId3Tag;
        this.fileWrapper = fileWrapper;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(LibraryItem... params) {
        for (LibraryItem item : params) {
            save(item);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onPostExecution();
    }

    public void save(LibraryItem item) {
        if (item.isAudioItem()) {
            try {
                AudioItem audioItem = (AudioItem) item;
                AudioFile audioFile = audioItem.getAudioFile();
                multipleId3Tag.update(audioFile.getId3Tag());
                audioItem.setAudioFile(audioFile);
                fileWrapper.save(audioFile);
            } catch (IOException e) {
                Log.w(this.getClass().toString(), e.getMessage(), e);
            }
        } else {
            FolderItem folderItem = (FolderItem) item;
            for (LibraryItem li : folderItem.getChildren()) {
                save(li);
            }
        }
    }

    public static interface Callback {
        public void onPostExecution();
    }
}
