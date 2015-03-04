package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.service.LibraryService;
import binauld.pierre.musictag.service.LibraryServiceImpl;
import binauld.pierre.musictag.service.state.MultipleTagContextualState;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

/**
 * Activity for editing audio tag of one or several track.
 */
public class TagFormActivity extends Activity implements ServiceConnection, View.OnClickListener {

    public static final int SUGGESTION_REQUEST_CODE = 1;

    private Resources res;

    private HashMap<SupportedTag, EditText> views = new HashMap<>();

    private TextView txt_filename;

    private String multipleTagMessage;

    private LibraryService service;
    private MultipleTagContextualState multipleTagContextualState;

    private FinishCallback finishCallback = new FinishCallback();
    private LoadingFinishedCallback loadingFinishedCallback = new LoadingFinishedCallback();
    private ProgressDialogCallback progressDialogCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init layout
        this.setContentView(R.layout.activity_tag_form);

        // Init action bar
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init Floating Action Button
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll_tag_form);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save);
        fab.setOnClickListener(this);

        // Init resources
        res = getResources();
        multipleTagMessage = res.getString(R.string.multiple_tag_message);

        // Init views
        initViews();
        progressDialogCallback = new ProgressDialogCallback();


        // Init service
        Intent intent = new Intent(this, LibraryServiceImpl.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (null != service) {
            unbindService(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag_form, menu);
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
            case R.id.action_suggestion:
                callSuggestionActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_save:
                saveContentAndFinish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SUGGESTION_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
//                    id3Tags = SharedObject.getId3Tags();
//                    loadContent(id3Tags);
                    break;
                case RESULT_CANCELED:
                default:
                    break;
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LibraryServiceImpl.LibraryServiceBinder binder = (LibraryServiceImpl.LibraryServiceBinder) service;

        this.service = binder.getService();
//        this.serviceState = this.service.getServiceState();
        this.multipleTagContextualState = this.service.getMultipleTagContextualState();

        if (null == this.multipleTagContextualState) {
            Log.e(this.getClass().toString(), "No contextual state has been provided.");
            finish();
        } else {
            // Load content

            List<Runnable> callbacks = new ArrayList<>();
            callbacks.add(loadingFinishedCallback);

            progressDialogCallback.initDialog(this, res.getString(R.string.loading),
                    res.getString(R.string.please_wait));
            callbacks.add(progressDialogCallback);

            multipleTagContextualState.launchComponentsLoading();
            multipleTagContextualState.launchMultiTagCreation(callbacks);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.service = null;
    }

    /**
     * Start the suggestion activity.
     */
    private void callSuggestionActivity() {
        progressDialogCallback.initDialog(this, res.getString(R.string.loading),
                res.getString(R.string.please_wait));

        MultipleId3Tag multipleId3Tag = updateMultipleId3TagFromViews();

        List<Runnable> callbacks = new ArrayList<>();
        callbacks.add(progressDialogCallback);
        callbacks.add(new CallSuggestionActivityCallback());

        multipleTagContextualState.launchUpdateModifiedId3Tag(multipleId3Tag, callbacks);

    }


    /**
     * Initialize views.
     */
    private void initViews() {
        ImageView img_artwork = (ImageView) findViewById(R.id.img_artwork);
        txt_filename = (TextView) findViewById(R.id.txt_filename);
        EditText txtTitle = ((FloatLabel) findViewById(R.id.txt_title)).getEditText();
        EditText txtArtist = ((FloatLabel) findViewById(R.id.txt_artist)).getEditText();
        EditText txtAlbum = ((FloatLabel) findViewById(R.id.txt_album)).getEditText();
        EditText txtYear = ((FloatLabel) findViewById(R.id.txt_year)).getEditText();
        EditText txtDisc = ((FloatLabel) findViewById(R.id.txt_disc)).getEditText();
        EditText txtTrack = ((FloatLabel) findViewById(R.id.txt_track)).getEditText();
        EditText txtGenre = ((FloatLabel) findViewById(R.id.txt_genre)).getEditText();

        views.put(SupportedTag.TITLE, txtTitle);
        views.put(SupportedTag.ARTIST, txtArtist);
        views.put(SupportedTag.ALBUM, txtAlbum);
        views.put(SupportedTag.YEAR, txtYear);
        views.put(SupportedTag.DISC_NO, txtDisc);
        views.put(SupportedTag.TRACK, txtTrack);
        views.put(SupportedTag.GENRE, txtGenre);

        //TODO: Workaround
        CardView cardArtwork = (CardView) findViewById(R.id.card_artwork);
        cardArtwork.setVisibility(View.GONE);
    }

    public void fillViews() {

        txt_filename.setText(multipleTagContextualState.getFilenames());

        MultipleId3Tag multipleId3Tag = multipleTagContextualState.getMultiTag();
        Id3Tag id3Tag = multipleId3Tag.getId3Tag();
        for (Map.Entry<SupportedTag, EditText> entry : views.entrySet()) {
            if (multipleId3Tag.isAMultipleTag(entry.getKey())) {
                entry.getValue().setText(multipleTagMessage);
            } else {
                entry.getValue().setText(id3Tag.get(entry.getKey()));
            }
        }
    }

    public MultipleId3Tag updateMultipleId3TagFromViews() {
        MultipleId3Tag multipleId3Tag = multipleTagContextualState.getMultiTag();

        for (Map.Entry<SupportedTag, EditText> entry : views.entrySet()) {
            String value = entry.getValue().getText().toString();
            //TODO: Find another way
            if (!value.equals(multipleTagMessage)) {
                multipleId3Tag.set(entry.getKey(), value);
            }
        }

        return multipleId3Tag;
    }

    public void saveContentAndFinish() {
        MultipleId3Tag multipleId3Tag = updateMultipleId3TagFromViews();

        List<Runnable> callbacks = new ArrayList<>();
        callbacks.add(finishCallback);
        
        if (multipleTagContextualState.getItemCount() != 1) {
            progressDialogCallback.initDialog(this, res.getString(R.string.saving),
                    res.getString(R.string.please_wait));
            callbacks.add(progressDialogCallback);
        }

        multipleTagContextualState.launchSaving(multipleId3Tag, callbacks);
    }


    class FinishCallback implements Runnable {

        @Override
        public void run() {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    class LoadingFinishedCallback implements Runnable {

        @Override
        public void run() {
            fillViews();
        }
    }

    class CallSuggestionActivityCallback implements Runnable {

        @Override
        public void run() {
            service.initSuggestionContextualState(multipleTagContextualState.getModifiedId3Tags());
            Intent intent = new Intent(TagFormActivity.this, SuggestionActivity.class);
            startActivityForResult(intent, SUGGESTION_REQUEST_CODE);
        }
    }

    class ProgressDialogCallback implements Runnable {

        private ProgressDialog dialog;

        public void initDialog(Context context, String title, String message) {
            if (null != dialog) {
                run();
            }
            dialog = ProgressDialog.show(context, title, message, true);
        }

        @Override
        public void run() {
            dialog.dismiss();
            dialog = null;
        }
    }
}