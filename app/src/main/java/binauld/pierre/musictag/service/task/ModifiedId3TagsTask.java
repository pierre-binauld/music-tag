package binauld.pierre.musictag.service.task;


import java.util.Map;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.tag.Id3Tag;

public class ModifiedId3TagsTask extends ActionTask<ModifiedId3TagsTask.ModifiedId3TagsAction> {

    private Map<AudioFile, Id3Tag> modifiedId3Tags;

    public ModifiedId3TagsTask(Map<AudioFile, Id3Tag> modifiedId3Tags) {
        this.modifiedId3Tags = modifiedId3Tags;
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (Map.Entry<AudioFile, Id3Tag> entry : modifiedId3Tags.entrySet()) {
            for (ModifiedId3TagsAction action : actions) {
                action.doAction(entry.getKey(), entry.getValue());
            }
        }

        return null;
    }

    public interface ModifiedId3TagsAction {
        void doAction(AudioFile audioFile, Id3Tag modifiedId3Tag);
    }
}