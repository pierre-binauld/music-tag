package binauld.pierre.musictag.service.action;


import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class MultipleTagAction implements ItemVisitor {

    private MultipleId3Tag multipleId3Tag;

    public MultipleTagAction(MultipleId3Tag multipleId3Tag) {
        this.multipleId3Tag = multipleId3Tag;
    }

    @Override
    public void visit(AudioFile audioFile) {
        multipleId3Tag.put(audioFile.getId3Tag());

    }

    @Override
    public void visit(Folder folder) {
    }
}
