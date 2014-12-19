package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

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

import java.io.File;
import java.io.IOException;

import binauld.pierre.musictag.R;

public class TagFormActivity extends Activity {
    private AudioFile audio;
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

            txt_title = (EditText) findViewById(R.id.txt_title);
            txt_artist = (EditText) findViewById(R.id.txt_artist);
            txt_album = (EditText) findViewById(R.id.txt_album);
            txt_year = (EditText) findViewById(R.id.txt_year);
            txt_album_artist = (EditText) findViewById(R.id.txt_album_artist);
            txt_composer = (EditText) findViewById(R.id.txt_composer);
            txt_grouping = (EditText) findViewById(R.id.txt_grouping);
            txt_genre = (EditText) findViewById(R.id.txt_genre);
            txt_disk = (EditText) findViewById(R.id.txt_disk);
            txt_track = (EditText) findViewById(R.id.txt_track);

            Tag tags = audio.getTag();
            txt_title.setText(tags.getFirst(FieldKey.TITLE));
            txt_artist.setText(tags.getFirst(FieldKey.ARTIST));
            txt_album.setText(tags.getFirst(FieldKey.ALBUM));
            txt_year.setText(tags.getFirst(FieldKey.YEAR));
            txt_album_artist.setText(tags.getFirst(FieldKey.ALBUM_ARTIST));
            txt_composer.setText(tags.getFirst(FieldKey.COMPOSER));
            txt_grouping.setText(tags.getFirst(FieldKey.GROUPING));
            txt_genre.setText(tags.getFirst(FieldKey.GENRE));
            txt_disk.setText(tags.getFirst(FieldKey.DISC_NO));
            txt_track.setText(tags.getFirst(FieldKey.TRACK));
        }
        else{
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendConfirmation(){
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
            tags.setField(FieldKey.TITLE,title);
            tags.setField(FieldKey.ARTIST,artist);
            tags.setField(FieldKey.ALBUM,album);
            tags.setField(FieldKey.YEAR,year);
            tags.setField(FieldKey.ALBUM_ARTIST,album_artist);
            tags.setField(FieldKey.COMPOSER,composer);
            tags.setField(FieldKey.GROUPING,grouping);
            tags.setField(FieldKey.GENRE,genre);
            tags.setField(FieldKey.DISC_NO,disk);
            tags.setField(FieldKey.TRACK,track);
        } catch (FieldDataInvalidException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
        audio.setTag(tags);
        try {
            audio.commit();
        } catch (CannotWriteException e) {
            e.printStackTrace();
        }
    }
}