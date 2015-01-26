package binauld.pierre.musictag.tag;

import java.util.HashMap;

/**
 * Represent a bundle of id3 tag supported by the app.
 */
public class Id3Tag extends HashMap<SupportedTag, String> {

    /**
     * Update tag from another tag.
     * @param in The other tag.
     */
    public void put(Id3Tag in) {
        for(Entry<SupportedTag, String> entry : in.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
}
