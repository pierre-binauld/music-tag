package binauld.pierre.musictag.wrapper.jaudiotagger;

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class NumericTagSetter implements TagSetter {
    @Override
    public void setTagField(Tag tags, FieldKey key, String value) throws FieldDataInvalidException {
        //TODO: if it is blank then remove
        if (!StringUtils.isBlank(value) && StringUtils.isNumeric(value)) {
            tags.setField(key, value);
        }
    }
}
