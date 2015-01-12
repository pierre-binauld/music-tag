package binauld.pierre.musictag.io;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.musicbrainz.Controller.Artist;
import org.musicbrainz.Controller.Recording;
import org.musicbrainz.modelWs2.Entity.RecordingWs2;
import org.musicbrainz.modelWs2.SearchResult.ArtistResultWs2;
import org.musicbrainz.modelWs2.SearchResult.RecordingResultWs2;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import binauld.pierre.musictag.adapter.SuggestionItemAdapter;
import binauld.pierre.musictag.item.SuggestionItem;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.Id3TagParcelable;
import binauld.pierre.musictag.tag.SupportedTag;

public class SuggestionLoader extends AsyncTask<Id3Tag, Integer, List<List<SuggestionItem>>> {

    private SuggestionItemAdapter adapter;
    private Runnable callback;

    public SuggestionLoader(SuggestionItemAdapter adapter, Runnable callback) {
        this.adapter = adapter;
        this.callback = callback;
    }

    @Override
    protected List<List<SuggestionItem>> doInBackground(Id3Tag... tags) {
        //TODO: To review
        List<List<SuggestionItem>> infoList = new ArrayList<>();

        HttpClient client = new DefaultHttpClient();

        int i = 0;
        publishProgress(0);
        for (Id3Tag tag : tags) {
//            infoList.add(search(client, tag));
            infoList.add(search(tag));
            publishProgress(++i);
        }

        return infoList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<List<SuggestionItem>> lists) {
        adapter.putSuggestions(lists);
        adapter.notifyDataSetChanged();
        callback.run();
    }

    public List<SuggestionItem> search(Id3Tag tag) {

        Recording recording = new Recording();
        recording.getSearchFilter().setLimit(20L);

//        recording.getSearchFilter().setTitle(tag.get(SupportedTag.TITLE));
        recording.getSearchFilter().setQuery(tag.get(SupportedTag.TITLE)
                + " AND artist:" + tag.get(SupportedTag.ARTIST)
                + " tnum:" + tag.get(SupportedTag.TRACK)
                + " release:" + tag.get(SupportedTag.ALBUM));

        recording.search(recording.getSearchFilter().getQuery());

        List<RecordingResultWs2> results = recording.getFullSearchResultList();

        List<SuggestionItem> items = new ArrayList<>();
        for(RecordingResultWs2 recordingResultWs2 : results) {
            RecordingWs2 recordingWs2 = recordingResultWs2.getRecording();

            Id3Tag resultTag = new Id3TagParcelable();
            resultTag.put(SupportedTag.TITLE, recordingWs2.getTitle());
            resultTag.put(SupportedTag.ARTIST, recordingWs2.getArtistCreditString());
            resultTag.put(SupportedTag.YEAR, recordingWs2.getReleases().get(0).getYear());
            resultTag.put(SupportedTag.ALBUM, recordingWs2.getReleases().get(0).getTitle());
            resultTag.put(SupportedTag.TRACK, ""+ recordingWs2.getReleases().get(0).getMediumList().getMedia().get(0).getPosition());

            SuggestionItem item = new SuggestionItem(resultTag, recordingResultWs2.getScore());
            items.add(item);
//            Log.wtf(this.getClass().toString(), item.getTitle());
        }

        return items;
    }

    @Deprecated
    private List<SuggestionItem> search(HttpClient client, Id3Tag tag) {
        Set<SuggestionItem> infosSet = new HashSet<>();

        try {
            String title = tag.get(SupportedTag.TITLE);
            String artist = tag.get(SupportedTag.ARTIST);
            String album = tag.get(SupportedTag.ALBUM);
            infosSet.addAll(searchForQuery(client, title, artist, album));
        } catch (KeyNotFoundException ignored) {
        }

//        title = tag.get(SupportedTag.TITLE);
//        int pos = title.lastIndexOf(".");
//        if (pos > 0)
//            title = title.substring(0, pos);
//        infosSet.addAll(searchForQuery(client, title, ""));
//
//        String[] parts = title.split("-|—|–", 2);
//        if (parts.length > 1) {
//            infosSet.addAll(searchForQuery(client, parts[0], parts[1]));
//            infosSet.addAll(searchForQuery(client, parts[1], parts[0]));
//        }

        //TODO: improvement ?
        List<SuggestionItem> infos = new ArrayList<>(infosSet);
        Collections.sort(infos, SuggestionItem.comparator);

        return infos;
    }

    @Deprecated
    private List<SuggestionItem> searchForQuery(HttpClient client, String title, String artist, String album) {
        List<SuggestionItem> infos = new ArrayList<>();

        HttpGet request;
        HttpResponse response;
        try {
            String url = "http://musicbrainz.org/ws/2/recording?fmt=json&query=" + URLEncoder.encode("\"" + title + "\"", "utf-8");
            if (StringUtils.isNotBlank(artist)) {
                url += URLEncoder.encode(" AND artist:\"" + artist + "\"", "utf-8");
            }
//            if(StringUtils.isNotBlank(album)) {
//                url += URLEncoder.encode(" AND album:\"" + album + "\"", "utf-8");
//            }

            Log.d(this.getClass().toString(), url);
            request = new HttpGet(url);
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                String body = IOUtils.toString(response.getEntity().getContent());
                JSONObject json = new JSONObject(body);
                JSONArray recordings = json.getJSONArray("recordings");

                for (int i = 0; i < recordings.length(); i++) {
                    try {
                        int recScore = Integer.parseInt(recordings.getJSONObject(i).getString("score"));
                        String recId = recordings.getJSONObject(i).getString("id");
                        String recTitle = recordings.getJSONObject(i).getString("title");
                        String recArtist = recordings.getJSONObject(i).getJSONArray("artist-credit").getJSONObject(0).getJSONObject("artist").getString("name");
                        String track = recordings.getJSONObject(i).getJSONArray("releases").getJSONObject(0).getString("track-count");
//                        String discNo = recordings.getJSONObject(i).getJSONArray("releases").getJSONObject(0).getJSONArray("media").getJSONObject(0).getString("disc-count");
                        String recAlbum = recordings.getJSONObject(i).getJSONArray("releases").getJSONObject(0).getString("title");
                        String year = recordings.getJSONObject(i).getJSONArray("releases").getJSONObject(0).getString("date").substring(0, 4);

                        Id3Tag tag = new Id3TagParcelable();
                        tag.put(SupportedTag.TITLE, recTitle);
                        tag.put(SupportedTag.ARTIST, recArtist);
                        tag.put(SupportedTag.TRACK, track);
//                      //  tag.put(SupportedTag.DISC_NO, discNo);
                        tag.put(SupportedTag.ALBUM, recAlbum);
////                        tag.put(SupportedTag.ALBUM_ARTIST, albumArtist);
////                        tag.put(SupportedTag.COMPOSER, composer);
////                        tag.put(SupportedTag.GENRE, genre);
////                        tag.put(SupportedTag.GROUPING, grouping);
                        tag.put(SupportedTag.YEAR, year);

                        SuggestionItem item = new SuggestionItem(tag, recScore);

                        infos.add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return infos;
    }

}
