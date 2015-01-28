package binauld.pierre.musictag.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.io.AsyncTaskExecutor;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.io.TagFormLoader;
import binauld.pierre.musictag.io.TagSaver;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.service.ArtworkService;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.Id3TagParcelable;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;
import binauld.pierre.musictag.wrapper.FileWrapper;
import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;

/**
 * Activity for editing audio tag of one or several track.
 */
public class TagFormActivity extends Activity implements View.OnClickListener {

    private static List<LibraryItem> providedItems;
    private static LibraryItemFactory providedItemFactory;

    public static void provideItems(List<LibraryItem> items) {
        TagFormActivity.providedItems = items;
    }

    public static void provideItemFactory(LibraryItemFactory itemFactory) {
        TagFormActivity.providedItemFactory = itemFactory;
    }

    public static final int SUGGESTION_REQUEST_CODE = 1;

    private Resources res;

    private ArtworkService artworkService;
    private LibraryItemFactory itemFactory;

    private LibraryItemLoaderManager loaderManager;
    private List<LibraryItem> items;
    private LibraryItem[] itemArray;
    private MultipleId3Tag multipleId3Tag;

    HashMap<SupportedTag, EditText> views = new HashMap<>();

    private ProgressDialog loadingDialog;
    private ProgressDialog savingDialog;

    private ImageView img_artwork;
    private TextView txt_filename;
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

    private String multipleTagMessage;
    FileWrapper wrapper = new JAudioTaggerWrapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init content
        if(initContent()) {
            loaderManager = new LibraryItemLoaderManager(itemFactory, 200);

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
            BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);
            artworkService = new ArtworkService(defaultArtworkBitmapDecoder);

            // Init views
            initViews();

            // Load content
            loadContent();

//        // Fill views
//        fillViews(id3Tags);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != loadingDialog) {
            loadingDialog.dismiss();
        }
        if(null != savingDialog) {
            savingDialog.dismiss();
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
                    Id3TagParcelable id3TagParcelable = data.getParcelableExtra(TagSuggestionActivity.TAG_KEY);
//                    id3Tags.put(id3TagParcelable.getId3Tag());
//                    fillViews(id3Tags);
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
        Intent intent = new Intent(this, TagSuggestionActivity.class);
//        intent.putExtra(TagSuggestionActivity.TAG_KEY, new Id3TagParcelable(id3Tags));
        startActivityForResult(intent, SUGGESTION_REQUEST_CODE);
    }


    /**
     * Initialize views.
     */
    private void initViews() {
        img_artwork = (ImageView) findViewById(R.id.img_artwork);
        txt_filename = (TextView) findViewById(R.id.txt_filename);
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

        views.put(SupportedTag.TITLE, txt_title);
        views.put(SupportedTag.ARTIST, txt_artist);
        views.put(SupportedTag.ALBUM, txt_album);
        views.put(SupportedTag.YEAR, txt_year);
        views.put(SupportedTag.DISC_NO, txt_disc);
        views.put(SupportedTag.TRACK, txt_track);
        views.put(SupportedTag.ALBUM_ARTIST, txt_album_artist);
        views.put(SupportedTag.COMPOSER, txt_composer);
        views.put(SupportedTag.GROUPING, txt_grouping);
        views.put(SupportedTag.GENRE, txt_genre);
    }

    public void fillViews(/*List<LibraryItem> items, MultipleId3Tag multipleId3Tag*/) {
        StringBuilder builder = new StringBuilder();
        for (LibraryItem item : items) {
            buildFilenameString(builder, item);
        }
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

    private void buildFilenameString(StringBuilder filenames, LibraryItem item) {
        if (item.isAudioItem()) {
            filenames.append(((AudioItem) item).getAudioFile().getFile().getAbsolutePath());
            filenames.append("\n");
        } else {
            NodeItem node = (NodeItem) item;
            for (LibraryItem child : node.getChildren()) {
                buildFilenameString(filenames, child);
            }
        }
    }

//    public boolean areItemsLoaded() {
//        boolean loaded = true;
//        for(LibraryItem item : items) {
//            if(!item.isAudioItem()) {
//                if(((NodeItem) item).getState() != LoadingState.LOADED) {
//                    loaded = false;
//                    break;
//                }
//            }
//        }
//
//        return loaded;
//    }

//    /**
//     * Fill views.
//     */
//    private void fillViews() {
//
//        boolean loaded = true;
//        String filenames = "";
//        for(LibraryItem item : audioItems) {
//            if(item.isAudioItem()) {
//                AudioItem audioItem = (AudioItem) item;
//                filenames += audioItem.getAudioFile().getFile().getAbsolutePath() + "\n";
//
//            } else if (loaded) {
//                NodeItem nodeItem = (NodeItem) item;
//
//                if(nodeItem.getState() != LoadingState.LOADED) {
//                    loaded = false;
//                }
//            }
//        }
//
//        txt_filename.setText(filenames.substring(0, filenames.length() - 2));
//
//        AudioFile audioFile = audioItems.getAudioFile();
//        Tag tags = audioFile.getTag();
//
//        Artwork artwork = tags.getFirstArtwork();
//        if (null != artwork) {
//            artworkService.setArtwork(audioItems, img_artwork, 200);
//        }
//
//        txt_filename.setText(audioFile.getFile().getAbsolutePath());
//        txt_title.setText(id3Tag.get(SupportedTag.TITLE));
//        txt_artist.setText(id3Tag.get(SupportedTag.ARTIST));
//        txt_album.setText(id3Tag.get(SupportedTag.ALBUM));
//        txt_year.setText(id3Tag.get(SupportedTag.YEAR));
//        txt_disc.setText(id3Tag.get(SupportedTag.DISC_NO));
//        txt_track.setText(id3Tag.get(SupportedTag.TRACK));
//        txt_album_artist.setText(id3Tag.get(SupportedTag.ALBUM_ARTIST));
//        txt_composer.setText(id3Tag.get(SupportedTag.COMPOSER));
//        txt_grouping.setText(id3Tag.get(SupportedTag.GROUPING));
//        txt_genre.setText(id3Tag.get(SupportedTag.GENRE));
//    }

//    private void updateId3TagFromViews() {
//        id3Tags.put(SupportedTag.TITLE, txt_title.getText().toString());
//        id3Tags.put(SupportedTag.ARTIST, txt_artist.getText().toString());
//        id3Tags.put(SupportedTag.ALBUM, txt_album.getText().toString());
//        id3Tags.put(SupportedTag.ALBUM_ARTIST, txt_album_artist.getText().toString());
//        id3Tags.put(SupportedTag.COMPOSER, txt_composer.getText().toString());
//        id3Tags.put(SupportedTag.GROUPING, txt_grouping.getText().toString());
//        id3Tags.put(SupportedTag.GENRE, txt_genre.getText().toString());
//        id3Tags.put(SupportedTag.YEAR, txt_year.getText().toString());
//        id3Tags.put(SupportedTag.DISC_NO, txt_disc.getText().toString());
//        id3Tags.put(SupportedTag.TRACK, txt_track.getText().toString());
//    }

    /**
     * Save the modification into the audio file and finish the activity.
     */
//    private void saveChangeAndFinish() {
//        Intent intent = new Intent();
//        try {
//            AudioFile audioFile = audioItems.getAudioFile();
//            updateId3TagFromViews();
//            jAudioTaggerWrapper.save(id3Tags, audioFile);
//
//            setResult(RESULT_OK, intent);
//        } catch (IOException e) {
//            Log.e(this.getClass().toString(), e.getMessage(), e);
//            setResult(RESULT_CANCELED, intent);
//        }
//
//        finish();
//    }

    /**
     * Initialize the audio item and finish if it is not possible.
     */
    public boolean initContent() {
        if (null == providedItems) {
            Log.e(this.getClass().toString(), "No item has been provided.");
            finish();
            return false;
        } else if (null == providedItemFactory) {
            Log.e(this.getClass().toString(), "No item factory has been provided.");
            finish();
            return false;
        } else {
            items = TagFormActivity.providedItems;
            itemArray = items.toArray(new LibraryItem[items.size()]);
            itemFactory =TagFormActivity.providedItemFactory;
            return true;
        }
    }


    private void loadContent() {

        loadingDialog = ProgressDialog.show(TagFormActivity.this, res.getString(R.string.loading),
                res.getString(R.string.please_wait), true);

        LibraryItemLoader.Callback tagFormLoaderLauncher = new LibraryItemLoader.Callback() {

            @Override
            public void onProgressUpdate(FolderItem item) {

            }

            @Override
            public void onPostExecute(List<FolderItem> results) {
                TagFormLoader.Callback callback = new TagFormLoader.Callback() {
                    @Override
                    public void onPostExecute(MultipleId3Tag multipleId3Tag) {
                        TagFormActivity.this.multipleId3Tag = multipleId3Tag;
                        fillViews(/*items, multipleId3Tag*/);
                        loadingDialog.dismiss();
                    }
                };
                AsyncTaskExecutor.execute(new TagFormLoader(callback), itemArray);
            }
        };

        AsyncTaskExecutor.execute(loaderManager.get(true, tagFormLoaderLauncher), itemArray);
    }

    public void saveContentAndFinish() {
        savingDialog = ProgressDialog.show(TagFormActivity.this, res.getString(R.string.saving),
                res.getString(R.string.please_wait), true);
        for (Map.Entry<SupportedTag, EditText> entry : views.entrySet()) {
            String value = entry.getValue().getText().toString();
            //TODO: Find another way
            if (!value.equals(multipleTagMessage)) {
                multipleId3Tag.set(entry.getKey(), value);
            }
        }

        TagSaver saver = new TagSaver(multipleId3Tag, wrapper, new TagSaver.Callback() {
            @Override
            public void onPostExecution() {
                savingDialog.dismiss();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        AsyncTaskExecutor.execute(saver, itemArray);
    }

//    public void attributeText(boolean boolTest, EditText txt_field, String currentText){
//        if(boolTest && !currentText.equals("")) {
//            txt_field.setText(currentText);
//        }else{
//            txt_field.setHint(txt_field.getHint() + alertMessage);
//        }
//    }

//    public void saveOneFile(boolean writeIfBlank) throws FieldDataInvalidException, CannotWriteException {
//        AudioFile audioFile = audioItems.getAudioFile();
//        Tag tags = audioFile.getTag();
////        setTagField(tags, FieldKey.TITLE, txt_title.getText().toString(), writeIfBlank);
////        setTagField(tags, FieldKey.ARTIST, txt_artist.getText().toString(), writeIfBlank);
////        setTagField(tags, FieldKey.ALBUM, txt_album.getText().toString(), writeIfBlank);
////        setTagField(tags, FieldKey.ALBUM_ARTIST, txt_album_artist.getText().toString(), writeIfBlank);
////        setTagField(tags, FieldKey.COMPOSER, txt_composer.getText().toString(), writeIfBlank);
////        setTagField(tags, FieldKey.GROUPING, txt_grouping.getText().toString(), writeIfBlank);
////        setTagField(tags, FieldKey.GENRE, txt_genre.getText().toString(), writeIfBlank);
////        setNumericTagField(tags, FieldKey.YEAR, txt_year.getText().toString());
////        setNumericTagField(tags, FieldKey.DISC_NO, txt_disk.getText().toString());
////        setNumericTagField(tags, FieldKey.TRACK, txt_track.getText().toString());
//        audioFile.setTag(tags);
//
//        AudioFileIO.write(audioFile);
//    }

//    public void saveSomeFiles() throws FieldDataInvalidException, CannotWriteException {
//
//        for(AudioItem audioIt : providedItems) {
//            audioItems = audioIt;
//            saveOneFile(false);
//        }
//    }


//    public void fillTextForOneAudio(){
//        audioItems = TagFormActivity.providedItems.get(0);
//        id3Tags = jAudioTaggerWrapper.build(audioItems.getAudioFile());
//    }
//
//    public void fillTextForSomeAudios(){
//        audioItems = TagFormActivity.providedItems.get(0);
//        id3Tags = jAudioTaggerWrapper.build(audioItems.getAudioFile());
//
//        boolean sameTitle = true, sameArtist = true, sameAlbum = true, sameYear = true,
//                sameDisk = true, sameTrack = true, sameAlbum_Artist = true,
//                sameComposer = true, sameGrouping = true, sameGenre = true;
//
//        String currentTitle = id3Tags.get(SupportedTag.TITLE);
//        String currentArtist = id3Tags.get(SupportedTag.ARTIST);
//        String currentAlbum = id3Tags.get(SupportedTag.ALBUM);
//        String currentYear = id3Tags.get(SupportedTag.YEAR);
//        String currentDisk = id3Tags.get(SupportedTag.DISC_NO);
//        String currentTrack = id3Tags.get(SupportedTag.TRACK);
//        String currentAlbum_Artist = id3Tags.get(SupportedTag.ALBUM_ARTIST);
//        String currentComposer = id3Tags.get(SupportedTag.COMPOSER);
//        String currentGrouping = id3Tags.get(SupportedTag.GROUPING);
//        String currentGenre = id3Tags.get(SupportedTag.GENRE);
//
//        String pathOfAllFiles = "";
//
//        for(AudioItem audio : providedItems){
//            pathOfAllFiles += audio.getAudioFile().getFile().getAbsolutePath() + " \n";
//            if(sameTitle || sameArtist || sameAlbum || sameYear ||
//                    sameDisk || sameTrack || sameAlbum_Artist ||
//                    sameComposer || sameGrouping || sameGenre){
//                id3Tags = jAudioTaggerWrapper.build(audio.getAudioFile());
//
//                if(sameTitle && !currentTitle.equals(id3Tags.get(SupportedTag.TITLE))){sameTitle = false;}
//                if(sameArtist && !currentArtist.equals(id3Tags.get(SupportedTag.ARTIST))){sameArtist = false;}
//                if(sameAlbum && !currentAlbum.equals(id3Tags.get(SupportedTag.ALBUM))){sameAlbum = false;}
//                if(sameYear && !currentYear.equals(id3Tags.get(SupportedTag.YEAR))){sameYear = false;}
//                if(sameDisk && !currentDisk.equals(id3Tags.get(SupportedTag.DISC_NO))){sameDisk = false;}
//                if(sameTrack && !currentTrack.equals(id3Tags.get(SupportedTag.TRACK))){sameTrack = false;}
//                if(sameAlbum_Artist && !currentAlbum_Artist.equals(id3Tags.get(SupportedTag.ALBUM_ARTIST))){sameAlbum_Artist = false;}
//                if(sameComposer && !currentComposer.equals(id3Tags.get(SupportedTag.COMPOSER))){sameComposer = false;}
//                if(sameGrouping && !currentGrouping.equals(id3Tags.get(SupportedTag.GROUPING))){sameGrouping = false;}
//                if(sameGenre && !currentGenre.equals(id3Tags.get(SupportedTag.GENRE))){sameGenre = false;}
//
//            }
//        }
//
//        txt_filename.setText(pathOfAllFiles.substring(0, pathOfAllFiles.length() - 2));
//
//        attributeText(sameTitle, txt_title, currentTitle);
//        attributeText(sameArtist, txt_artist, currentArtist);
//        attributeText(sameAlbum, txt_album, currentAlbum);
//        attributeText(sameYear, txt_year, currentYear);
//        attributeText(sameDisk, txt_disc, currentDisk);
//        attributeText(sameTrack, txt_track, currentTrack);
//        attributeText(sameAlbum_Artist, txt_album_artist, currentAlbum_Artist);
//        attributeText(sameComposer, txt_composer, currentComposer);
//        attributeText(sameGrouping, txt_grouping, currentGrouping);
//        attributeText(sameGenre, txt_genre, currentGenre);
//
//    }
}