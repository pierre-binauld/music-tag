package binauld.pierre.musictag.visitor;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;

public interface ItemVisitor {

    void visit(AudioFile audioFile);

    void visit(Folder folder);
}
