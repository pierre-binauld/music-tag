package binauld.pierre.musictag.file;


import android.util.Log;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongBrowser {

    public List<AudioFile> listSongs(String path) {
        List<AudioFile> result = new ArrayList<AudioFile>();

        File f = new File(path);

        for(String filename : f.list()) {
            if(filename.endsWith(".mp3")) {
                try {
                    result.add(new MP3File(path+"/"+filename));
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

        return result;
    }
}
