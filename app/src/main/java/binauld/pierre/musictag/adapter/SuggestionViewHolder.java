package binauld.pierre.musictag.adapter;


import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import binauld.pierre.musictag.R;

public class SuggestionViewHolder {

    protected CardView card;
    protected RadioButton rbItem;
    protected TextView txtTrack;
    protected TextView txtTitle;
    protected TextView txtArtist;
    protected TextView txtAlbum;
    protected TextView txtGenre;

    public SuggestionViewHolder(View v) {

        this.card = (CardView) v.findViewById(R.id.card_suggestion);
        this.rbItem = (RadioButton) v.findViewById(R.id.rb_suggestion);
        this.txtTrack = (TextView) v.findViewById(R.id.txt_track);
        this.txtTitle = (TextView) v.findViewById(R.id.txt_title);
        this.txtArtist = (TextView) v.findViewById(R.id.txt_artist);
        this.txtAlbum = (TextView) v.findViewById(R.id.txt_album);
        this.txtGenre = (TextView) v.findViewById(R.id.txt_genre);
    }
}
