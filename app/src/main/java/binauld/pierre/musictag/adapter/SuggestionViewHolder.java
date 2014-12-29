package binauld.pierre.musictag.adapter;


import android.view.View;
import android.widget.TextView;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.item.Suggestion;

public class SuggestionViewHolder {

//    protected CardView card;
//    protected RadioButton radio;
    protected TextView lblTrack;
    protected TextView lblTitle;
    protected TextView lblArtist;
    protected TextView lblAlbum;
    protected TextView lblGenre;

    public SuggestionViewHolder(View v) {

//        this.card = (CardView) v.findViewById(R.id.card_suggestion);
//        this.radio = (RadioButton) v.findViewById(R.id.radio_suggestion);
        this.lblTrack = (TextView) v.findViewById(R.id.lbl_track);
        this.lblTitle = (TextView) v.findViewById(R.id.lbl_title);
        this.lblArtist = (TextView) v.findViewById(R.id.lbl_artist);
        this.lblAlbum = (TextView) v.findViewById(R.id.lbl_album);
        this.lblGenre = (TextView) v.findViewById(R.id.lbl_genre);
    }

    public void fill(Suggestion item) {
        this.lblTrack.setText(item.getTrack());
        this.lblTitle.setText(item.getTitle());
        this.lblAlbum.setText(item.getTAlbum());
        this.lblArtist.setText(item.getArtist());
        this.lblGenre.setText(item.getGenre());
    }
}
