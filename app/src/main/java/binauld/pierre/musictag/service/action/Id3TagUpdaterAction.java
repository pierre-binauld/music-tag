package binauld.pierre.musictag.service.action;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.service.task.ModifiedId3TagsTask;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;

public class Id3TagUpdaterAction implements ModifiedId3TagsTask.ModifiedId3TagsAction {

    private MultipleId3Tag multipleId3Tag;

    public Id3TagUpdaterAction(MultipleId3Tag multipleId3Tag) {
        this.multipleId3Tag = multipleId3Tag;
    }

    @Override
    public void doAction(AudioFile audioFile, Id3Tag modifiedId3Tag) {
        multipleId3Tag.updateId3Tag(modifiedId3Tag);
    }
}
