package binauld.pierre.musictag.wrapper;

import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class StringTagSetter implements TagSetter {

    @Override
    public void setTagField(Tag tags, FieldKey key, String value) throws FieldDataInvalidException {
        if (StringUtils.isBlank(value)) {
            value = "";
        }
        tags.setField(key, value);
    }
}
