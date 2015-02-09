package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.loader.AsyncTaskExecutor;
import binauld.pierre.musictag.loader.OrganisationTask;
import binauld.pierre.musictag.util.SharedObject;

public class OrganisationActivity extends Activity implements View.OnClickListener, OrganisationTask.CallBack {
    private EditText placeholder;
    public List<LibraryComponent> libraryComponents;

    private Resources res;
    private SharedPreferences sharedPrefs;
    private String sourceFolder;
    private String placeholderSetting;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!initContent()) {
            finish();
        } else {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_organisation);
            placeholder = (EditText) findViewById(R.id.edit_text_organisation);

            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            res = getResources();
            sourceFolder = sharedPrefs.getString(
                    res.getString(R.string.source_folder_preference_key),
                    res.getString(R.string.source_folder_preference_default));
            if (sharedPrefs.contains("pref_placeholder")) {
                placeholderSetting = sharedPrefs.getString("pref_placeholder", "");
            } else {
                placeholderSetting = sharedPrefs.getString(
                        res.getString(R.string.placeholder_preference_key),
                        res.getString(R.string.placeholder_preference_default));
            }
            placeholder.setText(placeholderSetting);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.organisation_valid);
            fab.setOnClickListener(this);

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

    private boolean initContent() {
        libraryComponents = SharedObject.getComponents();
        return null != libraryComponents;
    }

    private void addContentToPlaceHolder(String s) {
        int cursorPosition = placeholder.getSelectionStart();
        String initText = placeholder.getText().toString();
        String newText = initText.substring(0,cursorPosition);
        newText += s;
        newText += initText.substring(cursorPosition);
        placeholder.setText(newText);
        placeholder.setSelection(cursorPosition + s.length());
    }

    private void processOrganisation() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("pref_placeholder", placeholder.getText().toString());
        editor.commit();

        OrganisationTask organisationTask = new OrganisationTask(placeholder.getText().toString(), sourceFolder, getString(R.string.unknown), this);
        LibraryComponent[] libraryComponentArray = libraryComponents.toArray(new LibraryComponent[libraryComponents.size()]);
        AsyncTaskExecutor.execute(organisationTask, libraryComponentArray);
        loadingDialog = ProgressDialog.show(OrganisationActivity.this, res.getString(R.string.saving),
                res.getString(R.string.please_wait), true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onPostExecute() {
        setResult(RESULT_OK);
        finish();
    }
}
