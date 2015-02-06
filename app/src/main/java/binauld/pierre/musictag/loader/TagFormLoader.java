package binauld.pierre.musictag.loader;


import android.os.AsyncTask;

import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;
import binauld.pierre.musictag.visitor.impl.ComponentVisitors;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class TagFormLoader extends AsyncTask<Id3Tag, Void, MultipleId3Tag> {

    private Callback callback;

    public TagFormLoader(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected MultipleId3Tag doInBackground(Id3Tag... params) {
        MultipleId3Tag multipleId3Tag = new MultipleId3Tag();

        for (Id3Tag id3Tag : params) {
            multipleId3Tag.put(id3Tag);
        }

        return multipleId3Tag;
    }


    @Override
    protected void onPostExecute(MultipleId3Tag multipleId3Tag) {
        callback.onPostExecute(multipleId3Tag);
    }

    public static interface Callback {
        public void onPostExecute(MultipleId3Tag multipleId3Tag);
    }
}
