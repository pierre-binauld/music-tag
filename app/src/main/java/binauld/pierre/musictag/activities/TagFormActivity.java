package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.iangclifton.android.floatlabel.FloatLabel;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import java.io.File;
import java.io.IOException;

import binauld.pierre.musictag.R;

public class TagFormActivity extends Activity {
    private AudioFile audio;
    private ImageView img_artwork;
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
        this.setContentView(R.layout.activity_tag_form);
        File file;
        setContentView(R.layout.activity_tag_form);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            file = (File) extras.getSerializable("file");
            try {
                audio = AudioFileIO.read(file);
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }

            img_artwork = (ImageView) findViewById(R.id.img_artwork);
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

            Tag tags = audio.getTag();
            Artwork artwork = tags.getFirstArtwork();
            if (null != artwork) {
                byte[] artworkData = artwork.getBinaryData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.length);
                img_artwork.setImageBitmap(bitmap);
            } else {
                findViewById(R.id.card_artwork).setVisibility(View.GONE);
            }
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
        } else {
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_valid:
                saveChange();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveChange() {
        String title = txt_title.getText().toString();
        String artist = txt_artist.getText().toString();
        String album = txt_album.getText().toString();
        String year = txt_year.getText().toString();
        String album_artist = txt_album_artist.getText().toString();
        String composer = txt_composer.getText().toString();
        String grouping = txt_grouping.getText().toString();
        String genre = txt_genre.getText().toString();
        String disk = txt_disk.getText().toString();
        String track = txt_track.getText().toString();

        Tag tags = audio.getTag();
        try {
            tags.setField(FieldKey.TITLE, title);
            tags.setField(FieldKey.ARTIST, artist);
            tags.setField(FieldKey.ALBUM, album);
            if (isInteger(year)) {
                tags.setField(FieldKey.YEAR, year);
            }
            tags.setField(FieldKey.ALBUM_ARTIST, album_artist);
            tags.setField(FieldKey.COMPOSER, composer);
            tags.setField(FieldKey.GROUPING, grouping);
            tags.setField(FieldKey.GENRE, genre);
            if (isInteger(disk)) {
                tags.setField(FieldKey.DISC_NO, disk);
            }
            if (isInteger(track)) {
                tags.setField(FieldKey.TRACK, track);
            }
        } catch (FieldDataInvalidException e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
        audio.setTag(tags);

        Intent intent = new Intent();
        try {
            AudioFileIO.write(audio);
            intent.putExtra("file", audio.getFile());
            setResult(RESULT_OK, intent);
        } catch (CannotWriteException e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
            setResult(RESULT_CANCELED, intent);
        }

        finish();
    }


    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}