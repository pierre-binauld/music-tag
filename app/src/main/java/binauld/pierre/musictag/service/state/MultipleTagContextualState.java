package binauld.pierre.musictag.service.state;


import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.MultipleId3Tag;

public interface MultipleTagContextualState {

    MultipleId3Tag getMultiTag();

    String getFilenames();

    Integer getItemCount();

    Map<AudioFile, Id3Tag> getModifiedId3Tags();

    void launchComponentsLoading();

    void launchMultiTagCreation(List<Runnable> callbacks);

    void launchSaving(MultipleId3Tag multipleId3Tag, List<Runnable> callbacks);

    void launchUpdateModifiedId3Tag(MultipleId3Tag multipleId3Tag, List<Runnable> callbacks);
}
