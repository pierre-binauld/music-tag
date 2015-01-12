package binauld.pierre.musictag.wrapper.jaudiotagger;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * A TagSetter can set a value of a FieldKey in a Tag.
 */
public interface TagSetter {

    /**
     * Set the value of the FieldKey in the Tag
     * @param tag The tag.
     * @param key The key.
     * @param value The value.
     * @throws FieldDataInvalidException Thrown if the data is invalid.
     */
    public void setTagField(Tag tag, FieldKey key, String value) throws FieldDataInvalidException;
}
