package binauld.pierre.musictag.search;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
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

import binauld.pierre.musictag.item.AudioItem;

/**
 * Created by mathieu on 19/12/2014.
 */
public class SearchTask extends AsyncTask<List<AudioItem>, Integer, List<List<RecordingInfo>>> {

	@Override
	protected List<List<RecordingInfo>> doInBackground(List<AudioItem>... libraryItemsList) {
		List<List<RecordingInfo>> infosList = new ArrayList<>();

		if (libraryItemsList.length > 0) {
			List<AudioItem> libraryItems = libraryItemsList[0];
			HttpClient client = new DefaultHttpClient();

			int i = 0;
			publishProgress(0);
			for (AudioItem item : libraryItems) {
				infosList.add(searchItem(client, item));
				publishProgress(++i);
			}
		}

		return infosList;
	}

	private List<RecordingInfo> searchItem(HttpClient client, AudioItem item) {
        Set<RecordingInfo> infosSet = new HashSet<>();

        Tag tag = item.getAudioFile().getTag();

        String title;
        String artist;

        try {
            title = tag.getFirst(FieldKey.TITLE);
            artist = tag.getFirst(FieldKey.ARTIST);
            infosSet.addAll(searchForQuery(client, title, artist));
        } catch (KeyNotFoundException ignored) {
        }

        title = item.getAudioFile().getFile().getName();
        int pos = title.lastIndexOf(".");
        if (pos > 0)
            title = title.substring(0, pos);
        infosSet.addAll(searchForQuery(client, title, ""));

        String[] parts = title.split("-|—|–", 2);
        if (parts.length > 1) {
            infosSet.addAll(searchForQuery(client, parts[0], parts[1]));
            infosSet.addAll(searchForQuery(client, parts[1], parts[0]));
        }

        List<RecordingInfo> infos = new ArrayList<>(infosSet);
        Collections.sort(infos, RecordingInfo.comparator);

		return infos;
	}

    private List<RecordingInfo> searchForQuery(HttpClient client, String title, String artist) {
        List<RecordingInfo> infos = new ArrayList<>();

        HttpGet request;
        HttpResponse response;
        try {
            String url = "http://musicbrainz.org/ws/2/recording?fmt=json&query=" + URLEncoder.encode("\""+title+"\"", "utf-8");
            if (artist != null && !artist.isEmpty())
                url += URLEncoder.encode(" AND artist:\""+artist+"\"", "utf-8");

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

                        RecordingInfo ii = new RecordingInfo(recId, recScore);
                        ii.setTitle(recTitle);
                        ii.setArtist(recArtist);

                        infos.add(ii);
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
