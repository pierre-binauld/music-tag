package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.io.SuggestionLoader;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.Id3TagParcelable;

/**
 * Activity for retrieve and choose tag suggestions from MusicBrainz.
 */
public class TagSuggestionActivity extends Activity implements View.OnClickListener {

    public static final int MAX_SCORE_PLUS_ONE = 101;
    public static final String TAG_KEY = "id3_tag";

    private Id3Tag id3Tag;
    private SuggestionItem localSuggestion;
    private SuggestionItemAdapter adapter;
    private SuggestionLoader loader;

    private ListView listView;
    private View waitingFooter;
    private View reloadFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_suggestion);

        // Init action bar
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init resources
        Resources res = getResources();

        // Init title
        setTitle(res.getString(R.string.title_activity_tag_suggestion));

        // Init content
        this.initContent();

        // Init adapter
        adapter = new SuggestionItemAdapter(localSuggestion, res);

        // Init footer
        waitingFooter = LayoutInflater.from(this).inflate(R.layout.suggestion_list_waiting_footer_view, listView, false);
        reloadFooter = LayoutInflater.from(this).inflate(R.layout.suggestion_list_retry_footer_view, listView, false);

        // Init List View
        listView = (ListView) findViewById(R.id.list_suggestion);
        listView.setAdapter(adapter);

        // Load content
        this.loadContent();

        // Init Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_valid);
        fab.attachToListView(listView);
        fab.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tag_suggestion, menu);
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

    @Override
    public void onBackPressed() {
        if(null != loader) {
            loader.cancel(true);
        }
        super.onBackPressed();
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
            intent.putExtra(TAG_KEY, new Id3TagParcelable(adapter.getSelectedSuggestion().getTag()));
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
        this.id3Tag = ((Id3TagParcelable) getIntent().getParcelableExtra(TAG_KEY)).getId3Tag();
        localSuggestion = new SuggestionItem(id3Tag, MAX_SCORE_PLUS_ONE);
    }

    /**
     * Load suggestions or finish activity if Id3 tag has not been provided.
     */
    private void loadContent() {
        if (null == id3Tag) {
            Log.e(this.getClass().toString(), "No tags has been provided.");
            finishWithCanceledResult();
        } else if(!isNetworkAvailable()) {
            //TODO: When retry, progress bar is weird.
            changeFooter(reloadFooter);
            Button retry = (Button) findViewById(R.id.button_retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadContent();
                }
            });
        } else {
            changeFooter(waitingFooter);
            loader = new SuggestionLoader(adapter, new Runnable() {
                @Override
                public void run() {
                    changeFooter(null);
                }
            });
            loader.execute(id3Tag);
        }
    }

    /**
     * Change the list view footer.
     * If footer is null, then just remove the footer.
     * @param footer The footer.
     */
    private void changeFooter(View footer) {
        listView.removeFooterView(waitingFooter);
        listView.removeFooterView(reloadFooter);
        if(null != footer) {
            listView.addFooterView(footer);
        }
    }

    /**
     * Check the network is available.
     * @return A boolean.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
