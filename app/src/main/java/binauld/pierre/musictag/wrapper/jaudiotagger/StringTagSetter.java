package binauld.pierre.musictag.wrapper.jaudiotagger;

import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Allow to set a string value of a FieldKey in a Tag.
 */
public class StringTagSetter implements TagSetter {

    @Override
    public void setTagField(Tag tags, FieldKey key, String value) throws FieldDataInvalidException {
        if (StringUtils.isBlank(value)) {
            value = "";
        }
        tags.setField(key, value);
    }
}
