package binauld.pierre.musictag.io;

import android.os.AsyncTask;

import java.util.ArrayList;
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
public class SuggestionLoader extends AsyncTask<Id3Tag, List<SuggestionItem>, Integer> {

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

    @Override
    protected Integer doInBackground(Id3Tag... tags) {

        int count = 0;
        for (Id3Tag tag : tags) {
            musicBrainzWrapper.initQuery(tag, 3L);
            //TODO: Magic number !
            for(int i=0; i<7;i++) {
                List<SuggestionItem> items = new ArrayList<>();
                List<ScoredId3Tag> resultTags = musicBrainzWrapper.nextSearchPage();
                for (ScoredId3Tag resultTag : resultTags) {
                    items.add(new SuggestionItem(resultTag.getTag(), resultTag.getScore()));
                }
//                Collections.sort(items, comparator);
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
