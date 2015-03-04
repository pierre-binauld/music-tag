package binauld.pierre.musictag.service.task;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;


public class SavingTask extends Task implements ComponentVisitor, ItemVisitor {

    private LibraryComponent component;
    private MultipleId3Tag multipleId3Tag;

    public SavingTask(MultipleId3Tag multipleId3Tag, LibraryComponent component) {
        this.multipleId3Tag = multipleId3Tag;
        this.component = component;
    }

    @Override
    protected Void doInBackground(Void... params) {
        component.accept(this);
        return null;
    }

    @Override
    public void visit(LibraryLeaf leaf) {
        leaf.getItem().accept(this);
    }

    @Override
    public void visit(LibraryComposite composite) {
        for (LibraryComponent component : composite.getChildren()) {
            component.accept(this);
        }
    }

    @Override
    public void visit(AudioFile audioFile) {

    }

    @Override
    public void visit(Folder folder) {

    }
}
