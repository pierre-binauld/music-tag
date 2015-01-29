package binauld.pierre.musictag.tag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultipleId3Tag {

    private Set<SupportedTag> isMultiple = new HashSet<>();
    private Id3Tag id3Tag = new Id3Tag();

    public void update(Id3Tag out) {
        for(Map.Entry<SupportedTag, String> entry : id3Tag.entrySet()) {
            if (!isAMultipleTag(entry.getKey())) {
                out.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void set(SupportedTag supportedTag, String value) {
        isMultiple.remove(supportedTag);
        id3Tag.put(supportedTag, value);
    }

    public void put(SupportedTag supportedTag, String value) {
        if (!isAMultipleTag(supportedTag)) {
            if (!id3Tag.containsKey(supportedTag)) {
                id3Tag.put(supportedTag, value);
            } else if(!id3Tag.get(supportedTag).equals(value)) {
                id3Tag.put(supportedTag, "");
                isMultiple.add(supportedTag);
            }
        }
    }

    public void put(Id3Tag in) {
        for(Map.Entry<SupportedTag, String> entry : in.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean isAMultipleTag(SupportedTag tag) {
        return isMultiple.contains(tag);
    }

    public Id3Tag getId3Tag() {
        return id3Tag;
    }
}
