package binauld.pierre.musictag.visitor.impl;


import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;

public abstract class AbstractComponentVisitor implements ComponentVisitor {

    protected ItemVisitor itemVisitor;

    public AbstractComponentVisitor(ItemVisitor itemVisitor) {
        this.itemVisitor = itemVisitor;
    }
}
