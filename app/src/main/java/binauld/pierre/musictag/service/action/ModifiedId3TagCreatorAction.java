package binauld.pierre.musictag.service.action;

import java.util.Map;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class ModifiedId3TagCreatorAction implements ItemVisitor {

    private Map<AudioFile, Id3Tag> modifiedId3Tag;

    public ModifiedId3TagCreatorAction(Map<AudioFile, Id3Tag> modifiedId3Tag) {
        this.modifiedId3Tag = modifiedId3Tag;
    }

    @Override
    public void visit(AudioFile audioFile) {
        modifiedId3Tag.put(audioFile, audioFile.getId3Tag());
    }

    @Override
    public void visit(Folder folder) {

    }
}
