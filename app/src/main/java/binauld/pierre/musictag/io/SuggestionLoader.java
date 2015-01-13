package binauld.pierre.musictag.io;

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
public class SuggestionLoader extends AsyncTask<Id3Tag, Integer, List<List<SuggestionItem>>> {

    private MusicBrainzWrapper musicBrainzWrapper = new MusicBrainzWrapper();
    private SuggestionItemAdapter adapter;
    private Runnable callback;

    public SuggestionLoader(SuggestionItemAdapter adapter, Runnable callback) {
        this.adapter = adapter;
        this.callback = callback;
    }

    @Override
    protected List<List<SuggestionItem>> doInBackground(Id3Tag... tags) {
        List<List<SuggestionItem>> suggestions = new ArrayList<>();

        for (Id3Tag tag : tags) {
            List<ScoredId3Tag> resultTags = musicBrainzWrapper.search(tag);
            List<SuggestionItem> items = new ArrayList<>();
            for (ScoredId3Tag resultTag : resultTags) {
                items.add(new SuggestionItem(resultTag.getTag(), resultTag.getScore()));
            }
            suggestions.add(items);
        }

        return suggestions;
    }

    @Override
    protected void onPostExecute(List<List<SuggestionItem>> lists) {
        adapter.putSuggestions(lists);
        adapter.notifyDataSetChanged();
        callback.run();
    }

}
