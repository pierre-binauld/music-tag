package binauld.pierre.musictag.adapter;

import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.item.SuggestionItem;

/**
 * Adapter for suggestion item.
 */
public class SuggestionItemAdapter extends BaseAdapter {

    private List<SuggestionItem> suggestionItems;
    private int selectedPosition;

    private int defaultBackgroundColor;
    private int localBackgroundColor;

    private int cardElevation;
    private int selectedCardElevation;


    public SuggestionItemAdapter(SuggestionItem suggestion, Resources res) {
        this.selectedPosition = 0;
        this.suggestionItems = new ArrayList<>();
        this.suggestionItems.add(suggestion);

        this.localBackgroundColor = res.getColor(R.color.cardview_background_local);
        this.defaultBackgroundColor = res.getColor(R.color.cardview_background);

        this.cardElevation = res.getInteger(R.integer.cardview_elevation);
        this.selectedCardElevation = res.getInteger(R.integer.cardview_elevation_selected);
    }

    @Override
    public int getCount() {
        return suggestionItems.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestionItems.get(position);
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
        SuggestionItem item = suggestionItems.get(position);
//TODO: strategy pattern
        // assign values if the object is not null
        if (item != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (0 == position) {
                    viewHolder.card.setBackgroundColor(localBackgroundColor);
                } else {
                    viewHolder.card.setBackgroundColor(defaultBackgroundColor);
                }
            }
            viewHolder.txtTrack.setText(item.getTrack());
            viewHolder.txtTitle.setText(item.getTitle());
            viewHolder.txtAlbum.setText(item.getTAlbum());
            viewHolder.txtArtist.setText(item.getArtist());
            viewHolder.txtYear.setText(item.getYear());

            viewHolder.rbItem.setChecked(position == selectedPosition);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewHolder.rbItem.isChecked()) {
                    viewHolder.card.setCardElevation(selectedCardElevation);
                } else {
                    viewHolder.card.setCardElevation(cardElevation);
                }
            }
            viewHolder.card.setTag(position);
            viewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = (Integer) view.getTag();
                    notifyDataSetChanged();
                }
            });

            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    /**
     * Check if the first item is selected.
     *
     * @return True if it is, else false.
     */
    public boolean isLocalSuggestionSelected() {
        if (0 == selectedPosition) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the current selected suggestion item.
     *
     * @return the current selected suggestion item.
     */
    public SuggestionItem getSelectedSuggestion() {
        return suggestionItems.get(selectedPosition);
    }

    public void putSuggestions(List<SuggestionItem> items) {
        this.suggestionItems.addAll(items);
    }
}
