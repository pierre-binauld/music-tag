package binauld.pierre.musictag.io;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.ScoredId3Tag;
import binauld.pierre.musictag.wrapper.musicbrainz.MusicBrainzWrapper;

/**
 * An AsyncTask which allow to load suggestions from MusicBrainz.
 */
public class SuggestionLoader extends AsyncTask<Id3Tag, Integer, List<SuggestionItem>> {

    private MusicBrainzWrapper musicBrainzWrapper = new MusicBrainzWrapper();
    private SuggestionItemAdapter adapter;
    private Runnable callback;

    private Comparator<SuggestionItem> comparator = new Comparator<SuggestionItem>() {
        @Override
        public int compare(SuggestionItem item1, SuggestionItem item2) {
            return item2.getScore() - item1.getScore();
        }
    };

    public SuggestionLoader(SuggestionItemAdapter adapter, Runnable callback) {
        this.adapter = adapter;
        this.callback = callback;
    }

    //TODO: Load asynchronously
    @Override
    protected List<SuggestionItem> doInBackground(Id3Tag... tags) {
        List<SuggestionItem> items = new ArrayList<>();

        for (Id3Tag tag : tags) {
            List<ScoredId3Tag> resultTags = musicBrainzWrapper.search(tag);
            for (ScoredId3Tag resultTag : resultTags) {
                items.add(new SuggestionItem(resultTag.getTag(), resultTag.getScore()));
            }
        }
        Collections.sort(items, comparator);


        return items;
    }

    @Override
    protected void onPostExecute(List<SuggestionItem> items) {
        adapter.putSuggestions(items);
        adapter.notifyDataSetChanged();
        callback.run();
    }

}
