package binauld.pierre.musictag.file;


import java.io.File;

public class NodeItem implements LibraryItem{

    private File file;

    public NodeItem(File file) {
        this.file = file;
    }

    @Override
    public boolean isSong() {
        return false;
    }
}
