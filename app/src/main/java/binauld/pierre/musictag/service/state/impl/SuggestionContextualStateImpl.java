package binauld.pierre.musictag.service.state.impl;

import java.util.Map;

import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.service.state.SuggestionContextualState;
import binauld.pierre.musictag.tag.Id3Tag;

public class SuggestionContextualStateImpl extends SuggestionContextualState {

    private Map<AudioFile, Id3Tag> modifiedId3Tags;

    public SuggestionContextualStateImpl(Map<AudioFile, Id3Tag> modifiedId3Tags) {

        this.modifiedId3Tags = modifiedId3Tags;
    }
}
