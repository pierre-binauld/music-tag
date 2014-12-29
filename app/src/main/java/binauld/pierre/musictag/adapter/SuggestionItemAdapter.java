package binauld.pierre.musictag.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.item.LocalSuggestion;
import binauld.pierre.musictag.item.Suggestion;


public class SuggestionItemAdapter extends BaseAdapter {

    private List<Suggestion> suggestions;

    public SuggestionItemAdapter(LocalSuggestion suggestion) {
        this.suggestions = new ArrayList<>();
        this.suggestions.add(suggestion);
        this.suggestions.add(suggestion);
        this.suggestions.add(suggestion);
        this.suggestions.add(suggestion);
        this.suggestions.add(suggestion);
        this.suggestions.add(suggestion);
        this.suggestions.add(suggestion);
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SuggestionViewHolder viewHolder;

        if (convertView == null) {

            // inflate the layout
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.suggestion_item_view, parent, false);

            // well set up the ViewHolder
            viewHolder = new SuggestionViewHolder(convertView);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // just use the viewHolder
            viewHolder = (SuggestionViewHolder) convertView.getTag();
        }

        // object item based on the position
        Suggestion item = suggestions.get(position);

        // assign values if the object is not null
        if (item != null) {
            viewHolder.fill(item);
            convertView.setTag(viewHolder);
        }

        return convertView;
    }
}
