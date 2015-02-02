package binauld.pierre.musictag.visitor.impl;


import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class RestrictedScopeComponentVisitor extends AbstractComponentVisitor {


    public RestrictedScopeComponentVisitor(ItemVisitor itemVisitor) {
        super(itemVisitor);
    }

    @Override
    public void visit(LibraryLeaf leaf) {
        leaf.getItem().accept(itemVisitor);
    }

    @Override
    public void visit(LibraryComposite composite) {
        composite.getItem().accept(itemVisitor);
    }
}
