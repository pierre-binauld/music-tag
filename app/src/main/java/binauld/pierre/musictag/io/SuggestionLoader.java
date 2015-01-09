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
            infoList.add(search(client, tag));
            publishProgress(++i);
        }

        return infoList;
    }

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
