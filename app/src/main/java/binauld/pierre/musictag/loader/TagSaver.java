package binauld.pierre.musictag.loader;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.item.itemable.AudioFile;
import binauld.pierre.musictag.tag.MultipleId3Tag;
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
//                AudioItem audioItem = (AudioItem) item;
                AudioFile audioFile = (AudioFile)item.getItemable();
                multipleId3Tag.update(audioFile.getId3Tag());
                //TODO: warn primary info not updated
//                audioItem.setAudioFile(audioFile);
                fileWrapper.save(audioFile);
            } catch (IOException e) {
                Log.w(this.getClass().toString(), e.getMessage(), e);
            }
        } else {
            NodeItem nodeItem = (NodeItem) item;
            for (LibraryItem li : nodeItem.getChildren()) {
                save(li);
            }
        }
    }

    public static interface Callback {
        public void onPostExecution();
    }
}
