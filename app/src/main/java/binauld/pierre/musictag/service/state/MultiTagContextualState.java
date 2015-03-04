package binauld.pierre.musictag.service.state;


import java.util.List;

import binauld.pierre.musictag.tag.MultipleId3Tag;

public interface MultiTagContextualState {

    MultipleId3Tag getMultiTag();

    String getFilenames();

    Integer getItemCount();

    void launchComponentsLoading();

    void launchMultiTagCreation(List<Runnable> callbacks);

    void launchSaving(List<Runnable> callbacks);
}
