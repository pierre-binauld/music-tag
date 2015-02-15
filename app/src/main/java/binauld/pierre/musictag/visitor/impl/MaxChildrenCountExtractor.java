package binauld.pierre.musictag.visitor.impl;


import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class MaxChildrenCountExtractor implements ItemVisitor {

    private int listMaxSize = 0;

    @Override
    public void visit(AudioFile audioFile) {

    }

    @Override
    public void visit(Folder folder) {
        listMaxSize = folder.getMaxChildrenCount();
    }

    public int getListMaxSize() {
        return listMaxSize;
    }
}
