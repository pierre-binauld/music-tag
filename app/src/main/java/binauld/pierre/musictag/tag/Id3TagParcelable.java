package binauld.pierre.musictag.tag;


import android.os.Parcel;
import android.os.Parcelable;

import org.jaudiotagger.audio.AudioFile;

import java.util.Map;

public class Id3TagParcelable extends Id3Tag implements Parcelable {

    public static final Parcelable.Creator<Id3Tag> CREATOR = new Parcelable.Creator<Id3Tag>() {
        public Id3Tag createFromParcel(Parcel in) {
            return new Id3TagParcelable(in);
        }

        public Id3Tag[] newArray(int size) {
            return new Id3Tag[size];
        }
    };

    private Id3TagParcelable(Parcel in) {
        final int size = in.readInt();

        for (int i = 0; i < size; i++) {
            final SupportedTag tag = (SupportedTag) in.readSerializable();
            final String value = in.readString();

            tags.put(tag, value);
        }
    }

    public Id3TagParcelable() {

    }

    public Id3TagParcelable(AudioFile audioFile) {
        super(audioFile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(tags.size());

        for (Map.Entry<SupportedTag, String> entry : tags.entrySet()) {
            out.writeSerializable(entry.getKey());
            out.writeString(entry.getValue());
        }
    }

}
