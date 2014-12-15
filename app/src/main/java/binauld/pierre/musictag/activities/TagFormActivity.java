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
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import binauld.pierre.musictag.R;

public class TagFormActivity extends Activity {
    private AudioFile audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tag_form);
        String jsonAudioFile= "";
        setContentView(R.layout.activity_tag_form);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonAudioFile = extras.getString("song");
        }
        audio = new Gson().fromJson(jsonAudioFile, AudioFile.class);

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
        final EditText txt_title = (EditText) findViewById(R.id.txt_title);
        String title = txt_title.getText().toString();
        final EditText txt_artist = (EditText) findViewById(R.id.txt_artist);
        String artist = txt_artist.getText().toString();
        final EditText txt_album = (EditText) findViewById(R.id.txt_album);
        String album = txt_album.getText().toString();

        Tag tags = audio.getTag();
        try {
            tags.setField(FieldKey.TITLE,title);
            tags.setField(FieldKey.ARTIST,artist);
            tags.setField(FieldKey.ALBUM,album);
        } catch (FieldDataInvalidException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
        audio.setTag(tags);
    }
}
