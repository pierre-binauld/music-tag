package binauld.pierre.musictag.service;

import java.util.List;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.service.state.LibraryServiceState;
import binauld.pierre.musictag.service.state.MultiTagContextualState;

public interface LibraryService {

    ArtworkManager getArtworkManager();

    LibraryServiceState getServiceState();

    void initMultiTagContextualState(List<LibraryComponent> components);

    MultiTagContextualState getMultiTagContextualState();
}
