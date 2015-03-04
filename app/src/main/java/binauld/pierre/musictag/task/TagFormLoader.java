//package binauld.pierre.musictag.task;
//
//
//import android.os.AsyncTask;
//
//import binauld.pierre.musictag.tag.Id3Tag;
//import binauld.pierre.musictag.tag.MultipleId3Tag;
//
//public class TagFormLoader extends AsyncTask<Id3Tag, Void, MultipleId3Tag> {
//
//    private Callback callback;
//
//    public TagFormLoader(Callback callback) {
//        this.callback = callback;
//    }
//
//    @Override
//    protected MultipleId3Tag doInBackground(Id3Tag... params) {
//        MultipleId3Tag multipleId3Tag = new MultipleId3Tag();
//
//        for (Id3Tag id3Tag : params) {
//            multipleId3Tag.put(id3Tag);
//        }
//
//        return multipleId3Tag;
//    }
//
//
//    @Override
//    protected void onPostExecute(MultipleId3Tag multipleId3Tag) {
//        callback.onPostExecute(multipleId3Tag);
//    }
//
//    public static interface Callback {
//        public void onPostExecute(MultipleId3Tag multipleId3Tag);
//    }
//}
