package binauld.pierre.musictag.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.fragments.SuggestionFragment;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.task.AsyncTaskExecutor;
import binauld.pierre.musictag.task.SuggestionLoader;
import binauld.pierre.musictag.tag.Id3Tag;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class SuggestionActivity extends MaterialNavigationDrawer implements SuggestionFragment.SuggestionInterface {

    public static final int MAX_SCORE_PLUS_ONE = 101;

    private int currentPosition = 0;
    private List<SuggestionLoader> suggestionLoaders = new ArrayList<>();
    private Map<AudioFile, SuggestionItemAdapter> adapters = new HashMap<>();
//    private List<AudioFile> audioFiles = new ArrayList<>();
    private Map<AudioFile, Id3Tag> id3Tags;

    @Override
    public void init(Bundle savedInstanceState) {
//        id3Tags = SharedObject.getId3Tags();
//        if(null == id3Tags) {
//            finish();
//        } else {
//            SuggestionFragment fragment = null;
//            for (Map.Entry<AudioFile, Id3Tag> entry : id3Tags.entrySet()) {
//                fragment = initFragment(entry);
//            }
//            fragment.setLastFragment(true);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO: does not work
        for (SuggestionLoader suggestionLoader : suggestionLoaders) {
            suggestionLoader.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (id3Tags.size() > 1) {
            getMenuInflater().inflate(R.menu.menu_suggestion, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_done_all:
                validAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public SuggestionFragment initFragment(Map.Entry<AudioFile, Id3Tag> entry) {
//        audioFiles.add(audioFile);
        AudioFile audioFile = entry.getKey();
        Id3Tag id3Tag = entry.getValue();

//        Bundle bundle = new Bundle();
//        bundle.putParcelable(SuggestionFragment.TAG_KEY, new Id3TagParcelable(id3Tag));

        SuggestionItem suggestionItem = new SuggestionItem(id3Tag, MAX_SCORE_PLUS_ONE);
        final SuggestionItemAdapter adapter = new SuggestionItemAdapter(suggestionItem, getResources());
        adapters.put(entry.getKey(), adapter);

        final SuggestionFragment fragment = new SuggestionFragment();
        fragment.setAdapter(adapter);
//        fragment.setArguments(bundle);

        SuggestionLoader suggestionLoader = new SuggestionLoader(id3Tag, new SuggestionLoader.Callback() {
            @Override
            public void onProgressUpdate(List<SuggestionItem>... values) {
                for (List<SuggestionItem> items : values) {
                    adapter.putSuggestions(items);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPostExecute(Integer count) {
                fragment.setLoadingFinished(true);
            }
        });
        AsyncTaskExecutor.execute(suggestionLoader);
        suggestionLoaders.add(suggestionLoader);

        MaterialSection section = newSection(audioFile.getPrimaryInformation(), fragment);
        addSection(section);

        return fragment;
    }

    @Override
    public void next() {
        currentPosition++;
        int size = getSectionList().size();
        if (currentPosition >= size) {
            currentPosition = 0;
        }

        this.setSection(this.getSectionAtCurrentPosition(currentPosition));
    }

    @Override
    public void valid() {
        validAndFinish();
    }

    private void validAndFinish() {
        for (AudioFile audioFile : adapters.keySet()) {
            id3Tags.put(audioFile, adapters.get(audioFile).getSelectedSuggestion().getTag());
        }
        //TODO: warn
        Intent intent = new Intent();
//        SharedObject.provideId3Tags(id3Tags);
        setResult(RESULT_OK, intent);
        finish();
    }


//    private void loadContent() {
//        if (null == id3Tag) {
//            Log.e(this.getClass().toString(), "No tags has been provided.");
//            finishWithCanceledResult();
//        } else if (!isNetworkAvailable()) {
    //TODO: When retry, progress bar is weird.
    //TODO: Animation
    //TODO: Do not recreate page when conf change.
    //TODO: be careful with selection when conf change.
//            fab.hide(true);
//            changeFooter(reloadFooter);
//            Button retry = (Button) findViewById(R.id.button_retry);
//            retry.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    loadContent();
//                }
//            });
//        } else {
//        fab.show(true);
//            changeFooter(waitingFooter);
//        loader = new SuggestionLoader(adapter, new Runnable() {
//            @Override
//            public void run() {
//                    changeFooter(null);
//            }
//        });
//        loader.execute(id3Tag);
//        }
//    }
}
