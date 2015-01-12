package binauld.pierre.musictag.wrapper.jaudiotagger;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public interface TagSetter {

    public void setTagField(Tag tag, FieldKey key, String value) throws FieldDataInvalidException;
}
