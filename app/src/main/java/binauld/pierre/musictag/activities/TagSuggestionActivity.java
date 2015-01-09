package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.io.SuggestionLoader;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.Id3TagParcelable;

public class TagSuggestionActivity extends Activity implements View.OnClickListener {

    public static final String TAG_KEY = "id3_tag";

    private Id3Tag id3Tag;
    private SuggestionItem localSuggestion;
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

        // Init content
        this.initContent();

        // Init adapter
        adapter = new SuggestionItemAdapter(localSuggestion);

        // Init List View
        final ListView listView = (ListView) findViewById(R.id.list_suggestion);
        final View footer = LayoutInflater.from(this).inflate(R.layout.suggestion_list_footer_view, listView, false);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);

        // Load content
        this.loadContent(new Runnable() {
            @Override
            public void run() {
                footer.setVisibility(View.GONE);
            }
        });

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
                returnSelectedTag();
                break;
        }
    }

    /**
     * Finish and return the selected tag if it is not the first one.
     * The first one is the one sent with the starting intent.
     */
    private void returnSelectedTag() {
        if (adapter.isLocalSuggestionSelected()) {
            finishWithCanceledResult();
        } else {
            Intent intent = new Intent();
            intent.putExtra(TAG_KEY, (Id3TagParcelable) adapter.getSelectedSuggestion().getTags());
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * Finish the activity with a canceled result.
     */
    private void finishWithCanceledResult() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * Initialize the first suggestion with the id3 tag passed by intent.
     * If it is null, then finish.
     */
    private void initContent() {
        this.id3Tag = (Id3Tag) getIntent().getParcelableExtra(TAG_KEY);
        if (null == id3Tag) {
            Log.e(this.getClass().toString(), "No tags has been provided.");
            finishWithCanceledResult();
        } else {
            localSuggestion = new SuggestionItem(id3Tag, 101);
        }
    }

    private void loadContent(Runnable callback) {
        SuggestionLoader loader = new SuggestionLoader(adapter, callback);
        loader.execute(id3Tag);
    }
}
