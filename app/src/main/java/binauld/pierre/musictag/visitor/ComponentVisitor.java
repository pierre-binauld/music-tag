package binauld.pierre.musictag.visitor;


import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;

public interface ComponentVisitor {

    void visit(LibraryLeaf leaf);

    void visit(LibraryComposite composite);
}
