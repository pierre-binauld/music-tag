package binauld.pierre.musictag.item;

import binauld.pierre.musictag.item.itemable.Itemable;

/**
 * Implement the parent management from the LibraryItem interface.
 */
public class ChildItem implements LibraryItem {

    protected NodeItem parent;
    protected Itemable itemable;

    public ChildItem(Itemable itemable) {
        this.itemable = itemable;
    }



    @Override
    public NodeItem getParent() {
        return parent;
    }

    /**
     * Set the parent.
     *
     * @param parent The parent to set.
     */
    public void setParent(NodeItem parent) {
        this.parent = parent;
    }

    @Override
    public Itemable getItemable() {
        return itemable;
    }

    @Override
    public boolean isAudioItem() {
        return true;
    }
//
//    @Override
//    public void setBitmapDecoder(BitmapDecoder decoder) {
//        this.decoder = decoder;
//    }
}
