package binauld.pierre.musictag.visitor.impl;


import android.util.Log;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class DrillDownComponentVisitor extends AbstractComponentVisitor {


    public DrillDownComponentVisitor(ItemVisitor itemVisitor) {
        super(itemVisitor);
    }

    @Override
    public void visit(LibraryLeaf leaf) {
        leaf.getItem().accept(itemVisitor);
    }

    @Override
    public void visit(LibraryComposite composite) {
        for (LibraryComponent component : composite.getChildren()) {
            component.accept(this);
        }
    }
}
