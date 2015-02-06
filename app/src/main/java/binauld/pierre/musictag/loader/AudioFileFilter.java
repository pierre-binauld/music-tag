package binauld.pierre.musictag.loader;


import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;
import binauld.pierre.musictag.visitor.impl.ComponentVisitors;

public class AudioFileFilter extends AsyncTask<LibraryComponent, Void, Map<AudioFile, Id3Tag>> implements ItemVisitor {

    private Map<AudioFile, Id3Tag> result;
    private Callback callback;

    public AudioFileFilter(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Map<AudioFile, Id3Tag> doInBackground(LibraryComponent... params) {
        result = new HashMap<>();

        ComponentVisitor filter = ComponentVisitors.buildDrillDownComponentVisitor(this);

        for (LibraryComponent component : params) {
            component.accept(filter);
        }

        return result;
    }

    @Override
    public void visit(AudioFile audioFile) {
        result.put(audioFile, audioFile.getId3Tag());
    }

    @Override
    public void visit(Folder folder) {

    }

    @Override
    protected void onPostExecute(Map<AudioFile, Id3Tag> audioFileId3TagMap) {
        callback.onPostExecute(audioFileId3TagMap);
    }

    public static interface Callback {
        public void onPostExecute(Map<AudioFile, Id3Tag> audioFileId3TagMap);
    }
}
