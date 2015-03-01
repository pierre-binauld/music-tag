package binauld.pierre.musictag.service;

import binauld.pierre.musictag.service.state.LibraryServiceState;

public interface LibraryService {

    ArtworkManager getArtworkManager();

    LibraryServiceState getServiceState();
}
