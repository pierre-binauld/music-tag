package binauld.pierre.musictag.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.ScoredId3Tag;
import binauld.pierre.musictag.wrapper.musicbrainz.MusicBrainzWrapper;

/**
 * An AsyncTask which allow to load suggestions from MusicBrainz.
 */
public class SuggestionLoader extends AsyncTask<Void, List<SuggestionItem>, Integer> {

    public static final Long UPDATE_STATE = 3L;
    public static final Integer UPDATE_STATE_COUNT = 7;

    private MusicBrainzWrapper musicBrainzWrapper = new MusicBrainzWrapper();
//    private SuggestionItemAdapter adapter;
    private Id3Tag id3Tag;
    private Callback callback;

    public SuggestionLoader(/*SuggestionItemAdapter adapter, */Id3Tag id3Tag, Callback callback) {
//        this.adapter = adapter;
        this.callback = callback;
        this.id3Tag = id3Tag;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        int count = 0;
//        for (Id3Tag tag : tags) {
            musicBrainzWrapper.initQuery(id3Tag, UPDATE_STATE);
            for(int i=0; i<UPDATE_STATE_COUNT;i++) {
                List<SuggestionItem> items = new ArrayList<>();
                //TODO: try catch
                List<ScoredId3Tag> resultTags = musicBrainzWrapper.nextSearchPage();
                for (ScoredId3Tag resultTag : resultTags) {
                    items.add(new SuggestionItem(resultTag.getTag(), resultTag.getScore()));
                }
                publishProgress(items);
                count += items.size();
            }
//        }

        return count;
    }

    @Override
    protected void onProgressUpdate(List<SuggestionItem>... values) {
//        for(List<SuggestionItem> items : values) {
//            adapter.putSuggestions(items);
//        }
//        adapter.notifyDataSetChanged();
        callback.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer count) {
        callback.onPostExecute(count);
    }

    public interface Callback {
        void onProgressUpdate(List<SuggestionItem>... values);
        void onPostExecute(Integer count);
    }

}
