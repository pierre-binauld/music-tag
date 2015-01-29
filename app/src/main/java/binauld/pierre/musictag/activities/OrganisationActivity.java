package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.melnykov.fab.FloatingActionButton;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class OrganisationActivity extends Activity implements View.OnClickListener {
    private EditText placeholder;
    public static List<LibraryItem> libraryItems;
    public static final int RELOAD_LIST = 10;
    private HashMap<SupportedTag, String> supportedPlaceholderMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_organisation);
        placeholder = (EditText) findViewById(R.id.edit_text_organisation);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.organisation_valid);
        fab.setOnClickListener(this);
        initPlaceholderMap();

        Button btn_title = (Button) findViewById(R.id.btn_title);
        btn_title.setOnClickListener(this);
        Button btn_artist = (Button) findViewById(R.id.btn_artist);
        btn_artist.setOnClickListener(this);
        Button btn_album = (Button) findViewById(R.id.btn_album);
        btn_album.setOnClickListener(this);
        Button btn_year = (Button) findViewById(R.id.btn_year);
        btn_year.setOnClickListener(this);
        Button btn_disc = (Button) findViewById(R.id.btn_disc);
        btn_disc.setOnClickListener(this);
        Button btn_track = (Button) findViewById(R.id.btn_track);
        btn_track.setOnClickListener(this);
        Button btn_album_artist = (Button) findViewById(R.id.btn_album_artist);
        btn_album_artist.setOnClickListener(this);
        Button btn_composer = (Button) findViewById(R.id.btn_composer);
        btn_composer.setOnClickListener(this);
        Button btn_grouping = (Button) findViewById(R.id.btn_grouping);
        btn_grouping.setOnClickListener(this);
        Button btn_genre = (Button) findViewById(R.id.btn_genre);
        btn_genre.setOnClickListener(this);
        Button btn_space = (Button) findViewById(R.id.btn_space);
        btn_space.setOnClickListener(this);
        Button btn_hyphen = (Button) findViewById(R.id.btn_hyphen);
        btn_hyphen.setOnClickListener(this);
        Button btn_underscore = (Button) findViewById(R.id.btn_underscore);
        btn_underscore.setOnClickListener(this);
        Button btn_slash = (Button) findViewById(R.id.btn_slash);
        btn_slash.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organisation, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.organisation_valid:
                processOrganisation();
                setResult(RELOAD_LIST);
                finish();
                break;
            case R.id.btn_title:
                addContentToPlaceHolder("[title]");
                break;
            case R.id.btn_artist:
                addContentToPlaceHolder("[artist]");
                break;
            case R.id.btn_album:
                addContentToPlaceHolder("[album]");
                break;
            case R.id.btn_year:
                addContentToPlaceHolder("[year]");
                break;
            case R.id.btn_disc:
                addContentToPlaceHolder("[disc]");
                break;
            case R.id.btn_track:
                addContentToPlaceHolder("[track]");
                break;
            case R.id.btn_album_artist:
                addContentToPlaceHolder("[album_artist]");
                break;
            case R.id.btn_composer:
                addContentToPlaceHolder("[composer]");
                break;
            case R.id.btn_grouping:
                addContentToPlaceHolder("[grouping]");
                break;
            case R.id.btn_genre:
                addContentToPlaceHolder("[genre]");
                break;
            case R.id.btn_slash:
                addContentToPlaceHolder("/");
                break;
            case R.id.btn_hyphen:
                addContentToPlaceHolder("-");
                break;
            case R.id.btn_underscore:
                addContentToPlaceHolder("_");
                break;
            case R.id.btn_space:
                addContentToPlaceHolder(" ");
                break;
            default:
                break;
        }
    }

    private void addContentToPlaceHolder(String s) {
        int cursorPosition = placeholder.getSelectionStart();
        String initText = placeholder.getText().toString();
        String newText = initText.substring(0,cursorPosition);
        newText += s;
        newText += initText.substring(cursorPosition);
        placeholder.setText(newText);
        placeholder.setSelection(cursorPosition+s.length());
    }

    private void initPlaceholderMap(){
        supportedPlaceholderMapping = new HashMap<>();
        supportedPlaceholderMapping.put(SupportedTag.TITLE, "\\[title\\]");
        supportedPlaceholderMapping.put(SupportedTag.ARTIST, "\\[artist\\]");
        supportedPlaceholderMapping.put(SupportedTag.ALBUM, "\\[album\\]");
        supportedPlaceholderMapping.put(SupportedTag.YEAR, "\\[year\\]");
        supportedPlaceholderMapping.put(SupportedTag.DISC_NO, "\\[disc\\]");
        supportedPlaceholderMapping.put(SupportedTag.TRACK, "\\[track\\]");
        supportedPlaceholderMapping.put(SupportedTag.ALBUM_ARTIST, "\\[album_artist\\]");
        supportedPlaceholderMapping.put(SupportedTag.COMPOSER, "\\[composer\\]");
        supportedPlaceholderMapping.put(SupportedTag.GROUPING, "\\[grouping\\]");
        supportedPlaceholderMapping.put(SupportedTag.GENRE, "\\[genre\\]");
    }

    private void filesSelection(List<LibraryItem> libItems){
        for(LibraryItem libraryItem: libItems){
            String placeholderContent = placeholder.getText().toString();
            if(libraryItem.isAudioItem()){
                AudioItem audioItem = (AudioItem) libraryItem;
                binauld.pierre.musictag.wrapper.AudioFile audioFile = audioItem.getAudioFile();
                File file = audioFile.getFile();
                Id3Tag id3Tag = audioFile.getId3Tag();
                for(Map.Entry<SupportedTag, String> entry : supportedPlaceholderMapping.entrySet()){
                    String tag;
                    if(id3Tag.containsKey(entry.getKey()) && !id3Tag.get(entry.getKey()).equals("")){
                        tag = id3Tag.get(entry.getKey());
                    }
                    else{
                        tag = getString(R.string.unknown);
                    }
                    placeholderContent = placeholderContent.replaceAll(entry.getValue(), tag);
                }
                placeholderContent = formatEndNameFile(placeholderContent, FilenameUtils.getExtension(file.getName()));
                String filePath = file.getPath();
                String oldPath = filePath.substring(0,filePath.lastIndexOf("/")) + "/" + file.getName();
                // TODO changer pour mettre le truc automatique
                placeholderContent = "/storage/emulated/0/Music/" + placeholderContent;
                moveFile(oldPath, placeholderContent);
            }
            else {
                NodeItem nodeItem = (NodeItem) libraryItem;
                filesSelection(nodeItem.getChildren());
            }
        }
        deleteEmptyFolders(new File("/storage/emulated/0/Music/"));
    }

    private void processOrganisation() {
        filesSelection(libraryItems);
    }

    private String formatEndNameFile(String path, String extension){
        File f = new File(path + "." + extension);
        int i = 1;
        do{
            if(f.exists()) {
                f = new File(path + " (" + i + ")." + extension);
                i++;
            }
        }while (f.exists());

        return f.getPath();
    }

    private void moveFile(String inputPath, String outputPath) {
        InputStream in;
        OutputStream out;
        try {
            int indexOfSlash = outputPath.lastIndexOf("/");
            String outputDir = outputPath.substring(0, indexOfSlash);
            //create output directory if it doesn't exist
            File dir = new File (outputDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            File f = new File(outputPath);
            int i = 1;
            do{
                if(f.exists()) {
                    int indexOfPoint = outputPath.lastIndexOf(".");
                    String outputBegin = outputPath.substring(0, indexOfPoint);
                    String outputEnd = outputPath.substring(indexOfPoint, outputPath.length());
                    f = new File(outputBegin + " (" + i + ")" + outputEnd);
                    i++;
                }
            }while (f.exists());

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(f.getPath());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();

            // delete the original file
            new File(inputPath).delete();
        }

        catch (FileNotFoundException e) {
            Log.e("File not found : Error during moveFile", e.getMessage());
        }
        catch (Exception e) {
            Log.e("Error during moveFile", e.getMessage());
        }

    }

    private boolean deleteEmptyFolders(File file){
        if (file.isDirectory()) {
            String[] children = file.list();
            if(children.length == 0){
                Log.e("To Delete", file.getName());
                file.delete();
                return true;
            }
            else {
                boolean test = false;
                for (int i = 0; i < children.length; i++) {
                    if(deleteEmptyFolders(new File(file, children[i]))){
                        test = true;
                    }
                }
                if(test) {
                    return deleteEmptyFolders(file);
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }
}
