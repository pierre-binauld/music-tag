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

public class TagFormLoader extends AsyncTask<LibraryComponent, Void, MultipleId3Tag> implements ItemVisitor {

    private Callback callback;
    private ComponentVisitor visitor;
    private MultipleId3Tag multipleId3Tag;

    public TagFormLoader(Callback callback) {
        this.callback = callback;
        this.visitor = ComponentVisitors.buildDrillDownComponentVisitor(this);
    }

    @Override
    protected MultipleId3Tag doInBackground(LibraryComponent... params) {

        multipleId3Tag = new MultipleId3Tag();

        for (LibraryComponent item : params) {
            item.accept(visitor);
        }

        return multipleId3Tag;
    }


    @Override
    protected void onPostExecute(MultipleId3Tag multipleId3Tag) {
        callback.onPostExecute(multipleId3Tag);
    }

    @Override
    public void visit(AudioFile audioFile) {
        Id3Tag id3Tag = audioFile.getId3Tag();

        multipleId3Tag.put(id3Tag);
    }

    @Override
    public void visit(Folder folder) {

    }

    public static interface Callback {
        public void onPostExecute(MultipleId3Tag multipleId3Tag);
    }
}
