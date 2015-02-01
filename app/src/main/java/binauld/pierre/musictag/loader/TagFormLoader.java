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

//        Result result = new Result();
        multipleId3Tag = new MultipleId3Tag();

        for (LibraryComponent item : params) {
//            updateMultipleId3Tag(item);
            item.accept(visitor);
        }

        return multipleId3Tag;
    }


    @Override
    protected void onPostExecute(MultipleId3Tag multipleId3Tag) {
//        for(Map.Entry<SupportedTag, String> entry : multipleId3Tag.getId3Tag().entrySet()) {
//            String value = entry.getValue();
//            if(multipleId3Tag.isAMultipleTag(entry.getKey())) {
//                //TODO: wtf ?!?
//                value = "multiple";
//            }
//        }
        callback.onPostExecute(multipleId3Tag);
    }

    @Override
    public void visit(AudioFile audioFile) {
        Id3Tag id3Tag = audioFile.getId3Tag();

//                result.tags.put(audioItem, id3Tag);
        multipleId3Tag.put(id3Tag);
    }

    @Override
    public void visit(Folder folder) {

    }

//    public void updateMultipleId3Tag(LibraryComponent item) {
//
//        if (item.isAudioItem()) {
//            Id3Tag id3Tag = ((AudioFile)item.getItem()).getId3Tag();
//
////                result.tags.put(audioItem, id3Tag);
//            multipleId3Tag.put(id3Tag);
//        } else {
//            LibraryComposite node = (LibraryComposite) item;
//            for(LibraryComponent li : node.getChildren()) {
//                updateMultipleId3Tag(li);
//            }
//        }
//    }

    public static interface Callback {
        public void onPostExecute(MultipleId3Tag multipleId3Tag);
    }

    //    static class Result {
//        private HashMap<AudioItem, Id3Tag> tags = new HashMap<>();
//        private MultipleId3Tag multipleId3Tag = new MultipleId3Tag();
//
//        public HashMap<AudioItem, Id3Tag> getTags() {
//            return tags;
//        }
//
//        public void setTags(HashMap<AudioItem, Id3Tag> tags) {
//            this.tags = tags;
//        }
//
//        public MultipleId3Tag getMultipleId3Tag() {
//            return multipleId3Tag;
//        }
//
//        public void setMultipleId3Tag(MultipleId3Tag multipleId3Tag) {
//            this.multipleId3Tag = multipleId3Tag;
//        }
//    }
}
