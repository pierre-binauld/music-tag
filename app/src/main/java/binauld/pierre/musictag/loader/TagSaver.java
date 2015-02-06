package binauld.pierre.musictag.loader;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;
import binauld.pierre.musictag.visitor.impl.ComponentVisitors;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.wrapper.FileWrapper;

public class TagSaver extends AsyncTask<Map<AudioFile, Id3Tag>, Void, Void> implements ItemVisitor {

    private MultipleId3Tag multipleId3Tag;
    private FileWrapper fileWrapper;
    private Callback callback;
    private ComponentVisitor visitor;

    public TagSaver(MultipleId3Tag multipleId3Tag, FileWrapper fileWrapper, Callback callback) {
        this.multipleId3Tag = multipleId3Tag;
        this.fileWrapper = fileWrapper;
        this.callback = callback;
        this.visitor = ComponentVisitors.buildDrillDownComponentVisitor(this);
    }

    @Override
    protected Void doInBackground(Map<AudioFile, Id3Tag>... params) {
        for (Map<AudioFile, Id3Tag> map : params) {
            for (Map.Entry<AudioFile, Id3Tag> entry : map.entrySet())
            try {
                AudioFile audioFile = entry.getKey();
                Id3Tag id3Tag = entry.getValue();

                multipleId3Tag.update(id3Tag);
                audioFile.setId3Tag(id3Tag);
                fileWrapper.save(audioFile);

            } catch (IOException e) {
                Log.w(this.getClass().toString(), e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onPostExecution();
    }

    public void save(LibraryComponent item) {
        item.accept(visitor);
    }

    @Override
    public void visit(AudioFile audioFile) {
        try {
            Id3Tag id3Tag = audioFile.getId3Tag();
            multipleId3Tag.update(id3Tag);
            audioFile.setId3Tag(id3Tag);
            fileWrapper.save(audioFile);
        } catch (IOException e) {
            Log.w(this.getClass().toString(), e.getMessage(), e);
        }
    }

    @Override
    public void visit(Folder folder) {
        // do nothing
    }

    public static interface Callback {
        public void onPostExecution();
    }
}
