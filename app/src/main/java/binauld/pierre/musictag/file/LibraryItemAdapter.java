package binauld.pierre.musictag.file;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class LibraryItemAdapter extends BaseAdapter {

    private List<LibraryItem> items = new ArrayList<LibraryItem>();

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        // TODO
        return null;
    }

    public List<LibraryItem> getItems() {
        return items;
    }
}
