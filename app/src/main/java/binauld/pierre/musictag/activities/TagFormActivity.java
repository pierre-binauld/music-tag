package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

import java.io.IOException;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.service.ArtworkService;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.Id3TagParcelable;
import binauld.pierre.musictag.tag.SupportedTag;
import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;

/**
 * Activity for editing audio tag of one or several track.
 */
public class TagFormActivity extends Activity implements View.OnClickListener {

    private static AudioItem providedItem;

    public static void provideItem(AudioItem item) {
        TagFormActivity.providedItem = item;
    }

    public static final int SUGGESTION_REQUEST_CODE = 1;

    private Resources res;

    private ArtworkService artworkService;

    private AudioItem audioItem;
    private Id3Tag id3Tag;

    private ImageView img_artwork;
    private TextView lbl_filename;
    private EditText txt_title;
    private EditText txt_artist;
    private EditText txt_album;
    private EditText txt_year;
    private EditText txt_album_artist;
    private EditText txt_composer;
    private EditText txt_grouping;
    private EditText txt_genre;
    private EditText txt_disc;
    private EditText txt_track;

    JAudioTaggerWrapper jAudioTaggerWrapper = new JAudioTaggerWrapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init content
        initContent();

        //Init layout
        this.setContentView(R.layout.activity_tag_form);

        // Init action bar
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init Floating Action Button
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll_tag_form);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_valid);
        fab.attachToScrollView(scrollView);
        fab.setOnClickListener(this);

        // Init resources
        res = getResources();

        // Init service(s)
        BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);
        artworkService = new ArtworkService(defaultArtworkBitmapDecoder);

        // Init views
        initViews();
        initActivityTitle();

        // Fill views
        fillViews(id3Tag);
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
            case R.id.fab_valid:
                saveChangeAndFinish();
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
                    Id3TagParcelable id3TagParcelable = data.getParcelableExtra(TagSuggestionActivity.TAG_KEY);
                    id3Tag = id3TagParcelable.getId3Tag();
                    fillViews(id3Tag);
                    break;
                case RESULT_CANCELED:
                default:
                    break;
            }
        }
    }

    /**
     * Start the suggestion activity.
     */
    private void callSuggestionActivity() {
        updateId3TagFromViews();
        Intent intent = new Intent(this, TagSuggestionActivity.class);
        intent.putExtra(TagSuggestionActivity.TAG_KEY, new Id3TagParcelable(id3Tag));
        startActivityForResult(intent, SUGGESTION_REQUEST_CODE);
    }

    /**
     * Initialize the audio item and finish if it is not possible.
     */
    private void initContent() {
        //TODO: maybe crash on suggestion return
        if (null == providedItem) {
            Log.e(this.getClass().toString(), "No item has been provided.");
            finish();
        } else {
            audioItem = TagFormActivity.providedItem;
            id3Tag = jAudioTaggerWrapper.build(audioItem.getAudioFile());
        }
    }

    /**
     * Initialize views.
     */
    private void initViews() {
        img_artwork = (ImageView) findViewById(R.id.img_artwork);
        lbl_filename = (TextView) findViewById(R.id.lbl_filename);
        txt_title = ((FloatLabel) findViewById(R.id.txt_title)).getEditText();
        txt_artist = ((FloatLabel) findViewById(R.id.txt_artist)).getEditText();
        txt_album = ((FloatLabel) findViewById(R.id.txt_album)).getEditText();
        txt_year = ((FloatLabel) findViewById(R.id.txt_year)).getEditText();
        txt_disc = ((FloatLabel) findViewById(R.id.txt_disc)).getEditText();
        txt_track = ((FloatLabel) findViewById(R.id.txt_track)).getEditText();
        txt_album_artist = ((FloatLabel) findViewById(R.id.txt_album_artist)).getEditText();
        txt_composer = ((FloatLabel) findViewById(R.id.txt_composer)).getEditText();
        txt_grouping = ((FloatLabel) findViewById(R.id.txt_grouping)).getEditText();
        txt_genre = ((FloatLabel) findViewById(R.id.txt_genre)).getEditText();
    }

    /**
     * Fill views.
     */
    private void fillViews(Id3Tag id3Tag) {
        AudioFile audioFile = audioItem.getAudioFile();
        Tag tags = audioFile.getTag();

        Artwork artwork = tags.getFirstArtwork();
        if (null != artwork) {
            artworkService.setArtwork(audioItem, img_artwork, 200);
        }

        lbl_filename.setText(audioFile.getFile().getAbsolutePath());
        txt_title.setText(id3Tag.get(SupportedTag.TITLE));
        txt_artist.setText(id3Tag.get(SupportedTag.ARTIST));
        txt_album.setText(id3Tag.get(SupportedTag.ALBUM));
        txt_year.setText(id3Tag.get(SupportedTag.YEAR));
        txt_disc.setText(id3Tag.get(SupportedTag.DISC_NO));
        txt_track.setText(id3Tag.get(SupportedTag.TRACK));
        txt_album_artist.setText(id3Tag.get(SupportedTag.ALBUM_ARTIST));
        txt_composer.setText(id3Tag.get(SupportedTag.COMPOSER));
        txt_grouping.setText(id3Tag.get(SupportedTag.GROUPING));
        txt_genre.setText(id3Tag.get(SupportedTag.GENRE));
    }

    private void updateId3TagFromViews() {
        id3Tag.put(SupportedTag.TITLE, txt_title.getText().toString());
        id3Tag.put(SupportedTag.ARTIST, txt_artist.getText().toString());
        id3Tag.put(SupportedTag.ALBUM, txt_album.getText().toString());
        id3Tag.put(SupportedTag.ALBUM_ARTIST, txt_album_artist.getText().toString());
        id3Tag.put(SupportedTag.COMPOSER, txt_composer.getText().toString());
        id3Tag.put(SupportedTag.GROUPING, txt_grouping.getText().toString());
        id3Tag.put(SupportedTag.GENRE, txt_genre.getText().toString());
        id3Tag.put(SupportedTag.YEAR, txt_year.getText().toString());
        id3Tag.put(SupportedTag.DISC_NO, txt_disc.getText().toString());
        id3Tag.put(SupportedTag.TRACK, txt_track.getText().toString());
    }

    /**
     * Save the modification into the audio file and finish the activity.
     */
    private void saveChangeAndFinish() {
        Intent intent = new Intent();
        try {
            AudioFile audioFile = audioItem.getAudioFile();
            updateId3TagFromViews();
            jAudioTaggerWrapper.save(id3Tag, audioFile);

            setResult(RESULT_OK, intent);
        } catch (IOException e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
            setResult(RESULT_CANCELED, intent);
        }

        finish();
    }

    /**
     * Initialize the activity title.
     */
    private void initActivityTitle() {
        String title = audioItem.getPrimaryInformation();
        if (StringUtils.isNotBlank(audioItem.getSecondaryInformation())) {
            title += " - " + audioItem.getSecondaryInformation();
        }
        setTitle(title);
    }
}