package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iangclifton.android.floatlabel.FloatLabel;

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.service.ArtworkService;

public class TagFormActivity extends Activity {

//    public static final String AUDIO_FILE_KEY = "audio_file";

    private static AudioItem providedItem;

    public static void provideItem(AudioItem item) {
        TagFormActivity.providedItem = item;
    }

    private ArtworkService artworkService;

    private AudioItem audioItem;

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
    private EditText txt_disk;
    private EditText txt_track;


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

        // Init service(s)
        artworkService = new ArtworkService(this, R.drawable.song);

        // Init views
        initContent();
        initActivityTitle();

    }

    public void initContent() {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
        if (null == providedItem) {
            Log.e(this.getClass().toString(), "No item has been provided.");
            finish();
        } else {
//            File file = (File) extras.getSerializable(AUDIO_FILE_KEY);
//            try {
//                audio = AudioFileIO.read(file);
//            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
//                e.printStackTrace();
//            }

            audioItem = TagFormActivity.providedItem;
            AudioFile audioFile = audioItem.getAudioFile();

            img_artwork = (ImageView) findViewById(R.id.img_artwork);
            lbl_filename = (TextView) findViewById(R.id.lbl_filename);
            txt_title = ((FloatLabel) findViewById(R.id.txt_title)).getEditText();
            txt_artist = ((FloatLabel) findViewById(R.id.txt_artist)).getEditText();
            txt_album = ((FloatLabel) findViewById(R.id.txt_album)).getEditText();
            txt_year = ((FloatLabel) findViewById(R.id.txt_year)).getEditText();
            txt_disk = ((FloatLabel) findViewById(R.id.txt_disk)).getEditText();
            txt_track = ((FloatLabel) findViewById(R.id.txt_track)).getEditText();
            txt_album_artist = ((FloatLabel) findViewById(R.id.txt_album_artist)).getEditText();
            txt_composer = ((FloatLabel) findViewById(R.id.txt_composer)).getEditText();
            txt_grouping = ((FloatLabel) findViewById(R.id.txt_grouping)).getEditText();
            txt_genre = ((FloatLabel) findViewById(R.id.txt_genre)).getEditText();

            Tag tags = audioFile.getTag();
            Artwork artwork = tags.getFirstArtwork();
            if (null != artwork) {
                artworkService.setArtwork(audioItem, img_artwork, 200);
//                byte[] artworkData = artwork.getBinaryData();
//                //TODO: Improvement needed.
//                // Use service Locator with weakreference for all factory needed ?
//
//                Bitmap bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length);
//                img_artwork.setImageBitmap(bitmap);
//            } else {
//                findViewById(R.id.card_artwork).setVisibility(View.GONE);
            }
            lbl_filename.setText(audioFile.getFile().getAbsolutePath());
            txt_title.setText(tags.getFirst(FieldKey.TITLE));
            txt_artist.setText(tags.getFirst(FieldKey.ARTIST));
            txt_album.setText(tags.getFirst(FieldKey.ALBUM));
            txt_year.setText(tags.getFirst(FieldKey.YEAR));
            txt_disk.setText(tags.getFirst(FieldKey.DISC_NO));
            txt_track.setText(tags.getFirst(FieldKey.TRACK));
            txt_album_artist.setText(tags.getFirst(FieldKey.ALBUM_ARTIST));
            txt_composer.setText(tags.getFirst(FieldKey.COMPOSER));
            txt_grouping.setText(tags.getFirst(FieldKey.GROUPING));
            txt_genre.setText(tags.getFirst(FieldKey.GENRE));
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
            case R.id.action_valid:
                saveChangeAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveChangeAndFinish() {
        Intent intent = new Intent();
        try {
            AudioFile audioFile = audioItem.getAudioFile();
            Tag tags = audioFile.getTag();
            setTagField(tags, FieldKey.TITLE, txt_title.getText().toString());
            setTagField(tags, FieldKey.ARTIST, txt_artist.getText().toString());
            setTagField(tags, FieldKey.ALBUM, txt_album.getText().toString());
            setTagField(tags, FieldKey.ALBUM_ARTIST, txt_album_artist.getText().toString());
            setTagField(tags, FieldKey.COMPOSER, txt_composer.getText().toString());
            setTagField(tags, FieldKey.GROUPING, txt_grouping.getText().toString());
            setTagField(tags, FieldKey.GENRE, txt_genre.getText().toString());
            setNumericTagField(tags, FieldKey.YEAR, txt_year.getText().toString());
            setNumericTagField(tags, FieldKey.DISC_NO, txt_disk.getText().toString());
            setNumericTagField(tags, FieldKey.TRACK, txt_track.getText().toString());
            audioFile.setTag(tags);

            AudioFileIO.write(audioFile);

//            intent.putExtra("file", audioFile.getFile());
            setResult(RESULT_OK, intent);
        } catch (CannotWriteException | FieldDataInvalidException e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
            setResult(RESULT_CANCELED, intent);
        }

        finish();
    }

    private void initActivityTitle() {
        String title = audioItem.getPrimaryInformation();
        if (StringUtils.isNotBlank(audioItem.getSecondaryInformation())) {
            title += " - " + audioItem.getSecondaryInformation();
        }
        setTitle(title);
    }

    private static void setNumericTagField(Tag tags, FieldKey key, String value) throws FieldDataInvalidException {
        if (!StringUtils.isBlank(value) && StringUtils.isNumeric(value)) {
            tags.setField(key, value);
        }
    }

    private static void setTagField(Tag tags, FieldKey key, String value) throws FieldDataInvalidException {
        if (StringUtils.isBlank(value)) {
            value = "";
        }
        tags.setField(key, value);
    }
}