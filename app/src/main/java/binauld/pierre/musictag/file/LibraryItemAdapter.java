package binauld.pierre.musictag.file;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;

public class LibraryItemAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView firstLine;
        TextView secondLine;
        ImageView thumbnail;
    }

    private Context context;
    private LayoutInflater inflater;
    private List<LibraryItem> items = new ArrayList<LibraryItem>();

    public LibraryItemAdapter(Context baseContext) {
        this.context = baseContext;
        this.inflater = LayoutInflater.from(this.context);
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

    /*
     * The convertView argument is essentially a "ScrapView" as described is Lucas post
     * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
     * It will have a non-null value when ListView is asking you recycle the row layout.
     * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
     */
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
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // object item based on the position
        LibraryItem item = items.get(position);

        // assign values if the object is not null
        if (item != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            viewHolder.firstLine.setText(item.getPrimaryInformation());
            viewHolder.secondLine.setText(item.getSecondaryInformation());
            viewHolder.thumbnail.setImageBitmap(item.getThumbnail());
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    public List<LibraryItem> getItems() {
        return items;
    }

}
