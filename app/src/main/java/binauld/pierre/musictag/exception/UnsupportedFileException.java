package binauld.pierre.musictag.exception;

import java.io.File;
import java.io.IOException;

public class UnsupportedFileException extends IOException {

    public UnsupportedFileException(File file) {
        super("The following file is unsupported by the app: " + file.getAbsolutePath());
    }
}
