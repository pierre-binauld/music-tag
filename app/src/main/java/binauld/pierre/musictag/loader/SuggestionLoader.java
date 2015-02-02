package binauld.pierre.musictag.loader;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.ScoredId3Tag;
import binauld.pierre.musictag.wrapper.musicbrainz.MusicBrainzWrapper;

/**
 * An AsyncTask which allow to load suggestions from MusicBrainz.
 */
public class SuggestionLoader extends AsyncTask<Id3Tag, List<SuggestionItem>, Integer> {

    public static final Long UPDATE_STATE = 3L;
    public static final Integer UPDATE_STATE_COUNT = 7;

    private MusicBrainzWrapper musicBrainzWrapper = new MusicBrainzWrapper();
    private SuggestionItemAdapter adapter;
    private Runnable callback;

    public SuggestionLoader(SuggestionItemAdapter adapter, Runnable callback) {
        this.adapter = adapter;
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(Id3Tag... tags) {

        int count = 0;
        for (Id3Tag tag : tags) {
            musicBrainzWrapper.initQuery(tag, UPDATE_STATE);
            for(int i=0; i<UPDATE_STATE_COUNT;i++) {
                List<SuggestionItem> items = new ArrayList<>();
                List<ScoredId3Tag> resultTags = musicBrainzWrapper.nextSearchPage();
                for (ScoredId3Tag resultTag : resultTags) {
                    items.add(new SuggestionItem(resultTag.getTag(), resultTag.getScore()));
                }
                publishProgress(items);
                count += items.size();
            }
        }

        return count;
    }

    @Override
    protected void onProgressUpdate(List<SuggestionItem>... values) {
        for(List<SuggestionItem> items : values) {
            adapter.putSuggestions(items);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer count) {
        callback.run();
    }

}
