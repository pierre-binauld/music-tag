package binauld.pierre.musictag.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    private LayoutInflater inflater;
    private List<LibraryItem> items = new ArrayList<LibraryItem>();

    public LibraryItemAdapter(Context baseContext) {
        this.inflater = LayoutInflater.from(baseContext);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
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
        LibraryItem item = items.get(position);

        // assign values if the object is not null
        if (item != null) {
            viewHolder.firstLine.setText(item.getPrimaryInformation());
            viewHolder.secondLine.setText(item.getSecondaryInformation());
            viewHolder.thumbnail.setImageBitmap(item.getThumbnail());
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    /**
     * Get the data model from the adapter.
     *
     * @return A list of library items.
     */
    public List<LibraryItem> getItems() {
        return items;
    }

}
