package binauld.pierre.musictag;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import binauld.pierre.musictag.file.LibraryItemAdapter;
import binauld.pierre.musictag.file.LibraryItemComparator;
import binauld.pierre.musictag.file.LibraryItemFilter;
import binauld.pierre.musictag.file.LibraryItemLoader;
import binauld.pierre.musictag.file.factory.LibraryItemFactory;
import binauld.pierre.musictag.file.factory.Mp3FileFactory;


public class MainActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);

        listView = (ListView) findViewById(R.id.library_item_list);

//        SimpleAdapter adapter = new SimpleAdapter(
//                this.getBaseContext(),
//                songs,
//                android.R.layout.simple_list_item_2,
//                new String[]{FieldKey.TITLE.name(), FieldKey.ARTIST.name()},
//                new int[]{android.R.id.text1, android.R.id.text2}
//        );

        LibraryItemLoader loader = buildAndSetLoaderAndAdapter();

        loader.execute(new File(Environment.getExternalStorageDirectory().toString() + "/Music"));
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

    private LibraryItemFactory buildLibraryItemFactory() {
        LibraryItemFactory factory = new LibraryItemFactory();

        factory.put(new Mp3FileFactory());

        return factory;
    }

    private LibraryItemLoader buildAndSetLoaderAndAdapter() {
        LibraryItemAdapter adapter = new LibraryItemAdapter(this.getBaseContext());

        LibraryItemFactory factory = buildLibraryItemFactory();
        LibraryItemComparator sorter = new LibraryItemComparator();
        LibraryItemFilter filter = new LibraryItemFilter(factory.getSupportedAudioFiles());

        listView.setAdapter(adapter);
        return new LibraryItemLoader(adapter, factory, sorter, filter);
    }
}
