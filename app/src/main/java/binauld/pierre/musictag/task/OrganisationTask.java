package binauld.pierre.musictag.task;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.SupportedTag;
import binauld.pierre.musictag.visitor.ItemVisitor;
import binauld.pierre.musictag.visitor.impl.DrillDownComponentVisitor;

public class OrganisationTask extends AsyncTask<LibraryComponent, Void, Void> implements ItemVisitor {
    private String pattern;
    private String sourceFolder;
    private String unknown;
    private HashMap<SupportedTag, String> supportedPlaceholderMapping;
    private WeakReference<CallBack> callBack;

    public OrganisationTask(String pattern, String sourceFolder, String unknown, CallBack callBack) {
        this.pattern = pattern;
        this.sourceFolder = sourceFolder;
        this.unknown = unknown;
        this.callBack = new WeakReference<CallBack>(callBack);
        initPlaceholderMap();
    }

    @Override
    protected Void doInBackground(LibraryComponent... components) {
        CustomDrillDownComponentVisitor visitor = new CustomDrillDownComponentVisitor(this);
        for (LibraryComponent component : components) {
            component.accept(visitor);
        }

        for (Item item : visitor.getFolders()) {
            item.accept(this);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (null != callBack.get()) {
            callBack.get().onPostExecute();
        }
    }

    private String formatEndNameFile(String path, String extension) {
        File f = new File(path + "." + extension);
        int i = 1;
        do {
            if (f.exists()) {
                f = new File(path + " (" + i + ")." + extension);
                i++;
            }
        } while (f.exists());

        return f.getPath();
    }

    private void initPlaceholderMap() {
        supportedPlaceholderMapping = new HashMap<>();
        supportedPlaceholderMapping.put(SupportedTag.TITLE, "\\[title\\]");
        supportedPlaceholderMapping.put(SupportedTag.ARTIST, "\\[artist\\]");
        supportedPlaceholderMapping.put(SupportedTag.ALBUM, "\\[album\\]");
        supportedPlaceholderMapping.put(SupportedTag.YEAR, "\\[year\\]");
        supportedPlaceholderMapping.put(SupportedTag.DISC_NO, "\\[disc\\]");
        supportedPlaceholderMapping.put(SupportedTag.TRACK, "\\[track\\]");
//        supportedPlaceholderMapping.put(SupportedTag.ALBUM_ARTIST, "\\[album_artist\\]");
//        supportedPlaceholderMapping.put(SupportedTag.COMPOSER, "\\[composer\\]");
//        supportedPlaceholderMapping.put(SupportedTag.GROUPING, "\\[grouping\\]");
        supportedPlaceholderMapping.put(SupportedTag.GENRE, "\\[genre\\]");
    }

    private void moveFile(String inputPath, String outputPath) {
        InputStream in;
        OutputStream out;
        try {
            int indexOfSlash = outputPath.lastIndexOf("/");
            String outputDir = outputPath.substring(0, indexOfSlash);
            //create output directory if it doesn't exist
            File dir = new File(outputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(outputPath);
            int i = 1;
            do {
                if (f.exists()) {
                    int indexOfPoint = outputPath.lastIndexOf(".");
                    String outputBegin = outputPath.substring(0, indexOfPoint);
                    String outputEnd = outputPath.substring(indexOfPoint, outputPath.length());
                    f = new File(outputBegin + " (" + i + ")" + outputEnd);
                    i++;
                }
            } while (f.exists());

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(f.getPath());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();

            // delete the original file
            new File(inputPath).delete();
        } catch (IOException e) {
            Log.e(this.getClass().toString(), e.getMessage());
        }

    }

    @Override
    public void visit(AudioFile audioFile) {
        String placeholderContent = new String(pattern);
        File file = audioFile.getFile();
        Id3Tag id3Tag = audioFile.getId3Tag();
        for (Map.Entry<SupportedTag, String> entry : supportedPlaceholderMapping.entrySet()) {
            String tag;
            if (id3Tag.containsKey(entry.getKey()) && !id3Tag.get(entry.getKey()).equals("")) {
                tag = id3Tag.get(entry.getKey());
            } else {
                tag = unknown;
            }
            placeholderContent = placeholderContent.replaceAll(entry.getValue(), tag);
        }
        placeholderContent = formatEndNameFile(placeholderContent, FilenameUtils.getExtension(file.getName()));
        String filePath = file.getPath();
        String oldPath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + file.getName();
        placeholderContent = sourceFolder + "/" + placeholderContent;
        moveFile(oldPath, placeholderContent);
    }

    @Override
    public void visit(Folder folder) {
        File[] children = folder.getFile().listFiles();
        if (children.length == 0) {
            folder.getFile().delete();
        }
    }

    class CustomDrillDownComponentVisitor extends DrillDownComponentVisitor {
        private List<Item> folders = new ArrayList<>();

        public CustomDrillDownComponentVisitor(ItemVisitor itemVisitor) {
            super(itemVisitor);
        }


        @Override
        public void visit(LibraryComposite composite) {
            super.visit(composite);
            folders.add(composite.getItem());
        }

        public List<Item> getFolders() {
            return folders;
        }
    }

    public static interface CallBack {
        public void onPostExecute();
    }
}
