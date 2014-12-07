package binauld.pierre.musictag;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.file.SongBrowser;
import binauld.pierre.musictag.file.SongLoader;


public class MainActivity extends Activity {

    private ListView songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Map<String, String>> songs = new ArrayList<Map<String, String>>();

//        SongBrowser browser = new SongBrowser();
//        for (AudioFile song : browser.listSongs(Environment.getExternalStorageDirectory().toString() + "/Music")) {
////            Log.d("ok", song.getTag().getFirst(FieldKey.TITLE));
//            HashMap<String, String> s = new HashMap<String, String>();
//            s.put("title", song.getTag().getFirst(FieldKey.TITLE));
//            s.put("artist", song.getTag().getFirst(FieldKey.ARTIST));
//            songs.add(s);
//        }

        songList = (ListView) findViewById(R.id.song_list);

        SimpleAdapter songAdapter = new SimpleAdapter(
                this.getBaseContext(),
                songs,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "artist"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        songList.setAdapter(songAdapter);

        SongLoader loader = new SongLoader(songAdapter, songs);
        Log.d(this.getClass().toString(), "coucou");
        loader.execute(new File(Environment.getExternalStorageDirectory().toString() + "/Music").listFiles());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
