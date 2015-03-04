package binauld.pierre.musictag.service.task;


import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class MultiTagTask extends Task implements ComponentVisitor, ItemVisitor {

    private MultipleId3Tag multipleId3Tag;
    private LibraryComponent component;

    public MultiTagTask(MultipleId3Tag multipleId3Tag, LibraryComponent composite) {
        this.multipleId3Tag = multipleId3Tag;
        this.component = composite;
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
        multipleId3Tag.put(audioFile.getId3Tag());
    }

    @Override
    public void visit(Folder folder) {
    }
}
