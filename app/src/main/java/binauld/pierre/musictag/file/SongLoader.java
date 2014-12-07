package binauld.pierre.musictag.file;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongLoader extends AsyncTask<File, AudioFile, Integer> {

    private BaseAdapter adapter;
    private List<Map<String, String>> songs;

    public SongLoader(BaseAdapter adapter, List<Map<String, String>> songs) {
        this.adapter = adapter;
        this.songs = songs;
    }

    @Override
    protected Integer doInBackground(File... files) {
        int count = 0;

//        List<AudioFile> result = new ArrayList<AudioFile>();

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".mp3")) {
                try {
                    publishProgress(new MP3File(files[i]));
//                    result.add(new MP3File(files[i]));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(this.getClass().toString(), e.getMessage(), e);
                } catch (TagException e) {
                    Log.e(this.getClass().toString(), e.getMessage(), e);
                } catch (ReadOnlyFileException e) {
                    Log.e(this.getClass().toString(), e.getMessage(), e);
                } catch (InvalidAudioFrameException e) {
                    Log.e(this.getClass().toString(), e.getMessage(), e);
                }
            }
        }

        return count;
    }

    @Override
    protected void onProgressUpdate(AudioFile... values) {
//        progressDialog.dismiss();

        for (int i = 0; i < values.length; i++) {
            HashMap<String, String> song = new HashMap<String, String>();
            song.put("title", values[i].getTag().getFirst(FieldKey.TITLE));
            song.put("artist", values[i].getTag().getFirst(FieldKey.ARTIST));
            songs.add(song);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}
