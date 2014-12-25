package binauld.pierre.musictag.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;
import binauld.pierre.musictag.service.ThumbnailService;

/**
 * Adapt a list of library item for a list view.
 */
public class LibraryItemAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView firstLine;
        TextView secondLine;
        ImageView thumbnail;
    }

    private NodeItem currentNode;
    private final ThumbnailService thumbnailService;
    private int thumbnailSize;
    private int invalidItemCount;
    private ProgressBar progressBar;

    public LibraryItemAdapter(ThumbnailService thumbnailService, int thumbnailSize) {
        this.thumbnailService = thumbnailService;
        this.thumbnailSize = thumbnailSize;
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
            thumbnailService.setThumbnail(item, viewHolder.thumbnail, thumbnailSize);
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

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

    private void updateProgressBar() {
        if (null != progressBar) {
            switch (currentNode.getState()) {
                case LOADING:
                    progressBar.setProgress(currentNode.size() + invalidItemCount);
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
     * Switch the current node to the parent node.
     *
     * @return True if the adapter has switch to the parent node.
     */
    public boolean backToParent() {
        NodeItem parent = currentNode.getParent();
        if (parent == null) {
            return false;
        } else {
            currentNode = parent;
            setUpProgressBar();
            return true;
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

    public void setInvalidItemCount(int invalidItemCount) {
        this.invalidItemCount = invalidItemCount;
    }

}
