package binauld.pierre.musictag.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Comparator;

import binauld.pierre.musictag.R;

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
    private LayoutInflater inflater;
//    private SortedArrayList<LibraryItem> items;
    private Comparator<LibraryItem> comparator;

    public LibraryItemAdapter(Context baseContext, Comparator<LibraryItem> comparator) {
        this.inflater = LayoutInflater.from(baseContext);
        this.comparator = comparator;
//        this.items = new SortedArrayList<LibraryItem>(this.comparator);
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
            convertView = inflater.inflate(R.layout.library_item_view, parent, false);

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
            viewHolder.thumbnail.setImageBitmap(item.getThumbnail());
            convertView.setTag(viewHolder);
        }

        return convertView;
    }


    public boolean backToParent() {
        NodeItem parent = currentNode.getParent();
        if(parent == null) {
            return false;
        } else {
            currentNode = parent;
            return true;
        }
    }

    public void setCurrentNode(NodeItem currentNode) {
        this.currentNode = currentNode;
    }

    public NodeItem getCurrentNode() {
        return currentNode;
    }

}
