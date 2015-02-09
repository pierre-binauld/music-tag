package binauld.pierre.musictag.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.task.SuggestionLoader;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.tag.Id3Tag;

/**
 * Activity for retrieve and choose tag suggestions from MusicBrainz.
 */
public class SuggestionFragment extends Fragment implements View.OnClickListener {

    public static final String TAG_KEY = "id3_tag";


    private boolean isLastFragment = false;

    private Id3Tag id3Tag;
    private SuggestionItem localSuggestion;
    private SuggestionItemAdapter adapter;
    private SuggestionLoader loader;

    private ListView listView;
    private View waitingFooter;
    private View reloadFooter;

    private FloatingActionButton fab;

    private SuggestionInterface callback;
    private boolean isLoadingFinished;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (SuggestionInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + SuggestionInterface.class.toString());
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Init content
//        if (this.initContent()) {

////            setContentView(R.layout.activity_tag_suggestion);
        View view = inflater.inflate(R.layout.activity_tag_suggestion, container, false);


        // Init List View
        listView = (ListView) view.findViewById(R.id.list_suggestion);
        listView.setAdapter(adapter);
//            // Init action bar
//            ActionBar actionBar = getActionBar();
//            if (null != actionBar) {
//                actionBar.setDisplayHomeAsUpEnabled(true);
//            }

//        // Init resources
//        Resources res = getResources();
//
////            // Init title
////            setTitle(res.getString(R.string.title_activity_tag_suggestion));
//
//        this.id3Tag = ((Id3TagParcelable) getArguments().getParcelable(TAG_KEY)).getId3Tag();
//        localSuggestion = new SuggestionItem(id3Tag, MAX_SCORE_PLUS_ONE);
//
//        // Init adapter
//        adapter = new SuggestionItemAdapter(localSuggestion, res);
//
        // Init footer
        if(!isLoadingFinished) {
            waitingFooter = inflater.inflate(R.layout.suggestion_list_waiting_footer_view, listView, false);
////            reloadFooter = inflater.inflate(R.layout.suggestion_list_retry_footer_view, listView, false);
            listView.addFooterView(waitingFooter);
        }
//
//        // Init List View
//        listView = (ListView) getView().findViewById(R.id.list_suggestion);
//        listView.setAdapter(adapter);
//
//        // Init Floating Action Button
//        fab = (FloatingActionButton) getView().findViewById(R.id.fab_valid);
//        fab.setOnClickListener(this);

        // Load content
//            this.loadContent();
//        }


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init resources
//        Resources res = getResources();

//            // Init title
//            setTitle(res.getString(R.string.title_activity_tag_suggestion));

//        this.id3Tag = ((Id3TagParcelable) getArguments().getParcelable(TAG_KEY)).getId3Tag();
//        localSuggestion = new SuggestionItem(id3Tag, MAX_SCORE_PLUS_ONE);
//
//        // Init adapter
//        adapter = new SuggestionItemAdapter(localSuggestion, res);

        // Init footer
//            waitingFooter = inflater.inflate(R.layout.suggestion_list_waiting_footer_view, listView, false);
//            reloadFooter = inflater.inflate(R.layout.suggestion_list_retry_footer_view, listView, false);


        // Init Floating Action Button
        fab = (FloatingActionButton) getView().findViewById(R.id.fab_next);
        fab.setOnClickListener(this);
        if (isLastFragment) {
            fab.setImageResource(R.drawable.ic_action_action_done_32dp);
        }

//        loadContent();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != loader) {
            loader.cancel(true);
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            case R.id.action_settings:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_next:
//                returnSelectedTag();
                if (isLastFragment) {
                    callback.valid();
                } else {
                    callback.next();
                }
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (null != loader) {
//            loader.cancel(true);
//        }
//        super.onBackPressed();
//    }

//    /**
//     * Finish and return the selected tag if it is not the first one.
//     * The first one is the one sent with the starting intent.
//     */
//    private void returnSelectedTag() {
//        if (adapter.isLocalSuggestionSelected()) {
//            finishWithCanceledResult();
//        } else {
//            Intent intent = new Intent();
//            intent.putExtra(TAG_KEY, new Id3TagParcelable(adapter.getSelectedSuggestion().getTag()));
//            setResult(RESULT_OK, intent);
//        }
//        finish();
//    }

//    /**
//     * Finish the activity with a canceled result.
//     */
//    private void finishWithCanceledResult() {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        finish();
//    }

//    /**
//     * Initialize the first suggestion with the id3 tag passed by intent.
//     * If it is null, then finish.
//     */
//    private boolean initContent() {
//        this.id3Tag = ((Id3TagParcelable) getIntent().getParcelableExtra(TAG_KEY)).getId3Tag();
//        localSuggestion = new SuggestionItem(id3Tag, MAX_SCORE_PLUS_ONE);
//        return true;
//    }

    /**
     * Load suggestions or finish activity if Id3 tag has not been provided.
     */
//    private void loadContent() {
//        if (null == id3Tag) {
//            Log.e(this.getClass().toString(), "No tags has been provided.");
//            finishWithCanceledResult();
//        } else if (!isNetworkAvailable()) {
    //TODO: When retry, progress bar is weird.
    //TODO: Animation
    //TODO: Do not recreate page when conf change.
//            fab.hide(true);
//            changeFooter(reloadFooter);
//            Button retry = (Button) findViewById(R.id.button_retry);
//            retry.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    loadContent();
//                }
//            });
//        } else {
//            fab.show(true);
//            changeFooter(waitingFooter);
//            loader = new SuggestionLoader(adapter, new Runnable() {
//                @Override
//                public void run() {
////                    changeFooter(null);
//                }
//            });
//            loader.execute(id3Tag);
//        }
//    }


    /**
     * Change the list view footer.
     * If footer is null, then just remove the footer.
     *
     * @param footer The footer.
     */
//    private void changeFooter(View footer) {
//        listView.removeFooterView(waitingFooter);
//        listView.removeFooterView(reloadFooter);
//        if (null != footer) {
//            listView.addFooterView(footer);
//        }
//    }

    /**
     * Check the network is available.
     *
     * @return A boolean.
     */
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    public void setLoadingFinished(boolean isLoadingFinished) {
        this.isLoadingFinished = isLoadingFinished;
        if(null != listView) {
            listView.removeFooterView(waitingFooter);
        }
    }

    public void setAdapter(SuggestionItemAdapter adapter) {
        this.adapter = adapter;
    }


    public boolean isLastFragment() {
        return isLastFragment;
    }

    public void setLastFragment(boolean isLastFragment) {
        this.isLastFragment = isLastFragment;
    }

    public interface SuggestionInterface {
        void next();

        void valid();
    }
}
