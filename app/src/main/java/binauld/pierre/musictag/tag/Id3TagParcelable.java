package binauld.pierre.musictag.tag;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * A wrapper of Id3Tag to make him parcelable.
 */
public class Id3TagParcelable implements Parcelable {

    public static final Parcelable.Creator<Id3TagParcelable> CREATOR = new Parcelable.Creator<Id3TagParcelable>() {
        public Id3TagParcelable createFromParcel(Parcel in) {
            return new Id3TagParcelable(in);
        }

        public Id3TagParcelable[] newArray(int size) {
            return new Id3TagParcelable[size];
        }
    };

    private Id3Tag id3Tag;

    private Id3TagParcelable(Parcel in) {
        this.id3Tag = new Id3Tag();
        final int size = in.readInt();

        for (int i = 0; i < size; i++) {
            final SupportedTag tag = (SupportedTag) in.readSerializable();
            final String value = in.readString();

            id3Tag.put(tag, value);
        }
    }

    public Id3TagParcelable(Id3Tag id3Tag) {
        this.id3Tag = id3Tag;
    }

    /**
     * Get the Id3Tag.
     * @return The Id3Tag.
     */
    public Id3Tag getId3Tag() {
        return id3Tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id3Tag.size());

        for (Map.Entry<SupportedTag, String> entry : id3Tag.entrySet()) {
            out.writeSerializable(entry.getKey());
            out.writeString(entry.getValue());
        }
    }
}
