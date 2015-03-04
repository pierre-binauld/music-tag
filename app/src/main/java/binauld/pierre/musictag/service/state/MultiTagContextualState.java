package binauld.pierre.musictag.service.state;


import binauld.pierre.musictag.tag.MultipleId3Tag;

public interface MultiTagContextualState {

    MultipleId3Tag getMultiTag();

    String getFilenames();

    void launchComponentsLoading();

    void launchMultiTagCreation(Runnable callback);

    void launchSaving(Runnable callback);
}
