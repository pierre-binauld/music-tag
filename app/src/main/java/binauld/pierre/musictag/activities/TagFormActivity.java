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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.service.ArtworkManager;
import binauld.pierre.musictag.service.LibraryService;
import binauld.pierre.musictag.service.LibraryServiceImpl;
import binauld.pierre.musictag.service.state.LibraryServiceState;
import binauld.pierre.musictag.service.state.MultiTagContextualState;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;
import binauld.pierre.musictag.task.AsyncTaskExecutor;
import binauld.pierre.musictag.task.TagFormLoader;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;
import binauld.pierre.musictag.visitor.impl.ComponentVisitors;

/**
 * Activity for editing audio tag of one or several track.
 */
public class TagFormActivity extends Activity implements ServiceConnection, View.OnClickListener {

    public static final int SUGGESTION_REQUEST_CODE = 1;

    private Resources res;

    private ArtworkManager artworkManager;
    private LibraryComponentFactory componentFactory;

    //    private LibraryComponentLoaderManager loaderManager;
    private List<LibraryComponent> components;
    private MultipleId3Tag multipleId3Tag;
    private Map<AudioFile, Id3Tag> id3Tags;

    private HashMap<SupportedTag, EditText> views = new HashMap<>();

    private TextView txt_filename;

    private String multipleTagMessage;
//    private FileWrapper wrapper = new JAudioTaggerWrapper();

    private LibraryService service;
    private LibraryServiceState serviceState;
    private MultiTagContextualState multiTagContextualState;

    private FinishCallback finishCallback = new FinishCallback();
    private LoadingCallback loadingCallback = new LoadingCallback();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init content
//        if(initContent()) {
//            loaderManager = new LibraryComponentLoaderManager(componentFactory, 200);

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

        // Init service(s)
//            BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);
//            artworkManager = new ArtworkManager(defaultArtworkBitmapDecoder, cacheService);

        // Init views
        initViews();


        // Init service
        Intent intent = new Intent(this, LibraryServiceImpl.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (null != loadingDialog) {
//            loadingDialog.dismiss();
//        }
//        if (null != savingDialog) {
//            savingDialog.dismiss();
//        }
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

    /**
     * Start the suggestion activity.
     */
    private void callSuggestionActivity() {
//        updateId3TagFromViews();
        Intent intent = new Intent(this, SuggestionActivity.class);
//        intent.putExtra(TagSuggestionActivity.TAG_KEY, new Id3TagParcelable(id3Tags));
//        SharedObject.provideId3Tags(id3Tags);
        startActivityForResult(intent, SUGGESTION_REQUEST_CODE);
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
//        EditText txtAlbumArtist = ((FloatLabel) findViewById(R.id.txt_album_artist)).getEditText();
//        EditText txtComposer = ((FloatLabel) findViewById(R.id.txt_composer)).getEditText();
//        EditText txtGrouping = ((FloatLabel) findViewById(R.id.txt_grouping)).getEditText();
        EditText txtGenre = ((FloatLabel) findViewById(R.id.txt_genre)).getEditText();

        views.put(SupportedTag.TITLE, txtTitle);
        views.put(SupportedTag.ARTIST, txtArtist);
        views.put(SupportedTag.ALBUM, txtAlbum);
        views.put(SupportedTag.YEAR, txtYear);
        views.put(SupportedTag.DISC_NO, txtDisc);
        views.put(SupportedTag.TRACK, txtTrack);
//        views.put(SupportedTag.ALBUM_ARTIST, txtAlbumArtist);
//        views.put(SupportedTag.COMPOSER, txtComposer);
//        views.put(SupportedTag.GROUPING, txtGrouping);
        views.put(SupportedTag.GENRE, txtGenre);

        //TODO: Workaround
        CardView cardArtwork = (CardView) findViewById(R.id.card_artwork);
        cardArtwork.setVisibility(View.GONE);
    }

    public void fillViews() {
        FilenameBuilderVisitor builderVisitor = new FilenameBuilderVisitor();
        ComponentVisitor componentVisitor = ComponentVisitors.buildDrillDownComponentVisitor(builderVisitor);

        for (LibraryComponent component : components) {
            component.accept(componentVisitor);
        }

        StringBuilder builder = builderVisitor.getBuilder();
        builder.deleteCharAt(builder.length() - 1);
        txt_filename.setText(builder.toString());

        Id3Tag id3Tag = multipleId3Tag.getId3Tag();
        for (Map.Entry<SupportedTag, EditText> entry : views.entrySet()) {
            if (multipleId3Tag.isAMultipleTag(entry.getKey())) {
                entry.getValue().setText(multipleTagMessage);
            } else {
                entry.getValue().setText(id3Tag.get(entry.getKey()));
            }
        }
    }

    /**
     * Initialize the audio item and finish if it is not possible.
     */
//    public boolean initContent() {
//        if (null == SharedObject.getComponents()) {
//            Log.e(this.getClass().toString(), "No item has been provided.");
//            finish();
//            return false;
//        } else if (null == SharedObject.getComponentFactory()) {
//            Log.e(this.getClass().toString(), "No component factory has been provided.");
//            finish();
//            return false;
//        } else {
//            components = SharedObject.getComponents();
//            componentFactory = SharedObject.getComponentFactory();
//            return true;
//        }
//    }
    private void loadContent() {

//        loadingDialog = ProgressDialog.show(TagFormActivity.this, res.getString(R.string.loading),
//                res.getString(R.string.please_wait), true);
//
//
////        final TagFormLoader.Callback finishLoading = new TagFormLoader.Callback() {
////            @Override
////            public void onPostExecute(MultipleId3Tag multipleId3Tag) {
////                TagFormActivity.this.multipleId3Tag = multipleId3Tag;
////                fillViews();
////                loadingDialog.dismiss();
////            }
////        };
//
//        final AudioFileFilter.Callback tagFormLoaderLauncher = new AudioFileFilter.Callback() {
//            @Override
//            public void onPostExecute(Map<AudioFile, Id3Tag> audioFileId3TagMap) {
////                TagFormActivity.this.id3Tags = audioFileId3TagMap;
////                Id3Tag[] id3TagArray = TagFormActivity.this.id3Tags.values().toArray(new Id3Tag[id3Tags.size()]);
////                AsyncTaskExecutor.execute(new TagFormLoader(finishLoading), id3TagArray);
//                loadContent(audioFileId3TagMap);
//            }
//        };
//
//        final LibraryComponent[] componentArray = components.toArray(new LibraryComponent[components.size()]);
//
//        LibraryComponentLoader.Callback filterLauncher = new LibraryComponentLoader.Callback() {
//
//            @Override
//            public void onProgressUpdate(LibraryComposite composite) {
//
//            }
//
//            @Override
//            public void onPostExecute(/*List<LibraryComposite> results*/) {
//                AsyncTaskExecutor.execute(new AudioFileFilter(tagFormLoaderLauncher), componentArray);
//            }
//        };

//        AsyncTaskExecutor.execute(loaderManager.get(true, filterLauncher), componentArray);
    }

//    private void loadContent(Map<AudioFile, Id3Tag> audioFileId3TagMap) {
//        loadingDialog.show();
//        final TagFormLoader.Callback finishLoading = new TagFormLoader.Callback() {
//            @Override
//            public void onPostExecute(MultipleId3Tag multipleId3Tag) {
//                TagFormActivity.this.multipleId3Tag = multipleId3Tag;
//                fillViews();
//                loadingDialog.dismiss();
//            }
//        };
//
//        TagFormActivity.this.id3Tags = audioFileId3TagMap;
//        Id3Tag[] id3TagArray = TagFormActivity.this.id3Tags.values().toArray(new Id3Tag[id3Tags.size()]);
//        AsyncTaskExecutor.execute(new TagFormLoader(finishLoading), id3TagArray);
//    }

    public void saveContentAndFinish() {
        multiTagContextualState.launchSaving(finishCallback);
//        savingDialog = ProgressDialog.show(TagFormActivity.this, res.getString(R.string.saving),
//                res.getString(R.string.please_wait), true);
//        for (Map.Entry<SupportedTag, EditText> entry : views.entrySet()) {
//            String value = entry.getValue().getText().toString();
//            //TODO: Find another way
//            if (!value.equals(multipleTagMessage)) {
//                multipleId3Tag.set(entry.getKey(), value);
//            }
//        }
//
//        TagSaver saver = new TagSaver(multipleId3Tag, wrapper, new TagSaver.Callback() {
//            @Override
//            public void onPostExecution() {
//                savingDialog.dismiss();
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
//
//        AsyncTaskExecutor.execute(saver, id3Tags);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LibraryServiceImpl.LibraryServiceBinder binder = (LibraryServiceImpl.LibraryServiceBinder) service;

        this.service = binder.getService();
        this.serviceState = this.service.getServiceState();
        this.multiTagContextualState = this.service.getMultiTagContextualState();

        if (null == this.multiTagContextualState) {
            Log.e(this.getClass().toString(), "No contextual state has been provided.");
            finish();
        } else {
            // Load content
            loadingCallback.initDialog();
            multiTagContextualState.launchComponentsLoadind();
            multiTagContextualState.launchMultiTagCreation(loadingCallback);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.service = null;
        this.serviceState = null;
    }

    class FilenameBuilderVisitor implements ItemVisitor {

        private StringBuilder builder;

        FilenameBuilderVisitor() {
            this.builder = new StringBuilder();
        }

        @Override
        public void visit(AudioFile audioFile) {
            builder.append(audioFile.getFile().getAbsolutePath());
            builder.append("\n");
        }

        @Override
        public void visit(Folder folder) {

        }

        public StringBuilder getBuilder() {
            return builder;
        }
    }

    class FinishCallback implements Runnable {

        private ProgressDialog savingDialog;

        public void initDialog() {
            savingDialog = ProgressDialog.show(TagFormActivity.this, res.getString(R.string.saving),
                    res.getString(R.string.please_wait), true);
        }

        @Override
        public void run() {
            savingDialog.dismiss();
        }
    }

    class LoadingCallback implements Runnable {

        private ProgressDialog loadingDialog;

        public void initDialog() {
            loadingDialog = ProgressDialog.show(TagFormActivity.this, res.getString(R.string.loading),
                    res.getString(R.string.please_wait), true);
        }

        @Override
        public void run() {
            loadingDialog.dismiss();
            multipleId3Tag = multiTagContextualState.getMultiTag();
            fillViews();
        }
    }
}