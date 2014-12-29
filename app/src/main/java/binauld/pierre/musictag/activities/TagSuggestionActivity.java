package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.LocalSuggestion;
import binauld.pierre.musictag.item.Suggestion;

public class TagSuggestionActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static AudioItem providedItem;

    public static void provideItem(AudioItem item) {
        TagSuggestionActivity.providedItem = item;
    }

    private AudioItem audioItem;
    private SuggestionItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_suggestion);

        // Init action bar
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init adapter
        this.initContent();

        // Init List View
        ListView listView = (ListView) findViewById(R.id.list_suggestion);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        // Init Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_valid);
        fab.attachToListView(listView);
        fab.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_suggestion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_valid:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Suggestion item = (Suggestion) adapterView.getItemAtPosition(position);
    }

    public void initContent() {
        if (null == providedItem) {
            Log.e(this.getClass().toString(), "No item has been provided.");
            finish();
        } else {
            this.audioItem = TagSuggestionActivity.providedItem;

            LocalSuggestion suggestion = new LocalSuggestion(audioItem.getAudioFile());

            // Init adapter
            adapter = new SuggestionItemAdapter(suggestion);
        }
    }
}
