package binauld.pierre.musictag.wrapper;

import java.io.File;
import java.io.IOException;

public interface FileWrapper {

    public AudioFile build(File file) throws IOException;

    public void save(AudioFile audioFile) throws IOException;
}
