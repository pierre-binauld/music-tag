package binauld.pierre.musictag.service.action;

import android.util.Log;

import java.io.IOException;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.service.task.ModifiedId3TagsTask;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;
import binauld.pierre.musictag.wrapper.FileWrapper;


public class SavingAction implements ModifiedId3TagsTask.ModifiedId3TagsAction {

    private FileWrapper fileWrapper;

    public SavingAction(FileWrapper fileWrapper) {
        this.fileWrapper = fileWrapper;
    }


    @Override
    public void doAction(AudioFile audioFile, Id3Tag modifiedId3Tag) {

        try {
            audioFile.setId3Tag(modifiedId3Tag);
            fileWrapper.save(audioFile);
        } catch (IOException e) {
            Log.w(this.getClass().toString(), e.getMessage(), e);
        }
    }
}
