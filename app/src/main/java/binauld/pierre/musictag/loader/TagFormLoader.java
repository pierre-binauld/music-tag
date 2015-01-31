package binauld.pierre.musictag.loader;


import android.os.AsyncTask;

import java.util.Map;

import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.item.itemable.AudioFile;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class TagFormLoader extends AsyncTask<LibraryItem, Void, MultipleId3Tag> {

    private Callback callback;

    public TagFormLoader(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected MultipleId3Tag doInBackground(LibraryItem... params) {

//        Result result = new Result();
        MultipleId3Tag multipleId3Tag = new MultipleId3Tag();

        for (LibraryItem item : params) {
            updateMultipleId3Tag(multipleId3Tag, item);
        }

        return multipleId3Tag;
    }

    public void updateMultipleId3Tag(MultipleId3Tag multipleId3Tag, LibraryItem item) {
        if (item.isAudioItem()) {
            Id3Tag id3Tag = ((AudioFile)item.getItemable()).getId3Tag();

//                result.tags.put(audioItem, id3Tag);
            multipleId3Tag.put(id3Tag);
        } else {
            NodeItem node = (NodeItem) item;
            for(LibraryItem li : node.getChildren()) {
                updateMultipleId3Tag(multipleId3Tag, li);
            }
        }
    }

    @Override
    protected void onPostExecute(MultipleId3Tag multipleId3Tag) {
        for(Map.Entry<SupportedTag, String> entry : multipleId3Tag.getId3Tag().entrySet()) {
            String value = entry.getValue();
            if(multipleId3Tag.isAMultipleTag(entry.getKey())) {
                value = "multiple";
            }
        }
        callback.onPostExecute(multipleId3Tag);
    }

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
