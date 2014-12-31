package binauld.pierre.musictag.adapter;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jaudiotagger.audio.AudioFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.activities.TagFormActivity;
import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.helper.LibraryItemFactoryHelper;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.service.ArtworkService;

/**
 * Adapt a list of library item for a list view.
 */
public class LibraryItemAdapter extends BaseAdapter {
    private List<AudioItem> audios;

    public void toggleSelection(int position) {
        LibraryItem item = (LibraryItem) getItem(position);
        if (item.isAudioItem()) {
            AudioItem audio = (AudioItem) item;
            toggleAudio(audio);
        }
        else{
            FolderItem folder = (FolderItem) item;
            List<File> files = recursiveDirectoryContent(folder);
            LibraryItemFactory factory = LibraryItemFactoryHelper.buildFactory(folder.getResources(), folder.getFilter(), new ResourceBitmapDecoder(folder.getResources(), R.drawable.list_item_placeholder));


            for(File f : files) {
                AudioItem audioItem = new AudioItem();
                audioItem.setParent(folder);
                try {
                    factory.update(audioItem, f);
                } catch (IOException e) {
                    Log.e(this.getClass().toString(), e.getMessage(), e);
                }
                audios.add(audioItem);
            }
        }
    }

    public void toggleAudio(AudioItem audio){
        if(audios.contains(audio)){
            audios.remove(audio);
        }else{
            audios.add(audio);
        }
    }

    public List<File> recursiveDirectoryContent(FolderItem folder){
        File[] files = folder.getFileList();
        List<File> returnfiles = new ArrayList<>();
        for(File f : files){
            if (f.isDirectory()){
                List<File> filesList = recursiveDirectoryContent(new FolderItem(f, folder.getFilter(), folder.getResources()));
                for(File fileRecurse : filesList) {
                    returnfiles.add(fileRecurse);
                }
            }
            else{
                returnfiles.add(f);
            }
        }
        return returnfiles;
    }

    public Intent sendSelection(Activity activity) {
        Intent intent = new Intent(activity, TagFormActivity.class);
        TagFormActivity.provideItem(audios);
        return intent;
    }

    public void resetSelection(){
        audios.clear();
    }

    static class ViewHolder {
        TextView firstLine;
        TextView secondLine;
        ImageView thumbnail;
    }

    private NodeItem currentNode;
    private final ArtworkService artworkService;
    private int artworkSize;
    private ProgressBar progressBar;

    public LibraryItemAdapter(ArtworkService artworkService, int artworkSize) {
        this.artworkService = artworkService;
        this.artworkSize = artworkSize;
        audios = new ArrayList<>();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateProgressBar();
    }

    @Override
    public int getCount() {
        return currentNode.size();
    }

    @Override
    public Object getItem(int i) {
        return currentNode.getChild(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate the layout
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.library_item_view, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.firstLine = (TextView) convertView.findViewById(R.id.first_line);
            viewHolder.secondLine = (TextView) convertView.findViewById(R.id.second_line);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // object item based on the position
        LibraryItem item = currentNode.getChild(position);

        // assign values if the object is not null
        if (item != null) {
            viewHolder.firstLine.setText(item.getPrimaryInformation());
            viewHolder.secondLine.setText(item.getSecondaryInformation());
            artworkService.setArtwork(item, viewHolder.thumbnail, artworkSize);
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    /**
     * Set the progress bar.
     * @param progressBar The progress bar.
     */
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * Initialize the progress bar (progress, max, visibility).
     */
    private void setUpProgressBar() {
        if (null != progressBar) {
            if (null == currentNode) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(currentNode.getMaxChildren());
                updateProgressBar();
            }
        }
    }

    /**
     * Update the progression of the progress bar.
     */
    private void updateProgressBar() {
        if (null != progressBar) {
            switch (currentNode.getState()) {
                case LOADING:
                    progressBar.setProgress(currentNode.size() + currentNode.getInvalidItemCount());
                    break;
                case LOADED:
                    progressBar.setVisibility(View.GONE);
                    break;
                case NOT_LOADED:
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Set the current node of the library tree list.
     *
     * @param currentNode The current node to set.
     */
    public void setCurrentNode(NodeItem currentNode) {
        this.currentNode = currentNode;
        setUpProgressBar();
    }

    /**
     * Get the current node display by the list.
     *
     * @return The current node to get.
     */
    public NodeItem getCurrentNode() {
        return currentNode;
    }

}
