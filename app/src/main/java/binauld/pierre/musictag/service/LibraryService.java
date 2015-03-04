package binauld.pierre.musictag.service;

import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.service.state.LibraryServiceState;
import binauld.pierre.musictag.service.state.MultipleTagContextualState;
import binauld.pierre.musictag.service.state.SuggestionContextualState;
import binauld.pierre.musictag.tag.Id3Tag;

public interface LibraryService {

    ArtworkManager getArtworkManager();

    LibraryServiceState getServiceState();

    void initMultipleTagContextualState(List<LibraryComponent> components);

    MultipleTagContextualState getMultipleTagContextualState();

    void clearMultipleTagContextualState();

    void initSuggestionContextualState(Map<AudioFile, Id3Tag> modifiedId3Tags);

    SuggestionContextualState getSuggestionContextualState();

    void clearSuggestionContextualState();
}
