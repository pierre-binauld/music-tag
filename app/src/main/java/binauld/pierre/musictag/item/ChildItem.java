package binauld.pierre.musictag.item;

import binauld.pierre.musictag.decoder.BitmapDecoder;

/**
 * Implement the parent management from the LibraryItem interface.
 */
public abstract class ChildItem implements LibraryItem {

    protected NodeItem parent;
    protected BitmapDecoder decoder;

    @Override
    public NodeItem getParent() {
        return parent;
    }

    /**
     * Set the parent.
     * @param parent The parent to set.
     */
    public void setParent(NodeItem parent) {
        this.parent = parent;
    }

    @Override
    public BitmapDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(BitmapDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public void switchDecoder(BitmapDecoder decoder) {
        this.decoder = decoder;
    }
}
