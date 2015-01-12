package binauld.pierre.musictag.wrapper;


import org.musicbrainz.Controller.Recording;
import org.musicbrainz.FilterWs2.SearchFilter.RecordingSearchFilterWs2;
import org.musicbrainz.modelWs2.Entity.RecordingWs2;
import org.musicbrainz.modelWs2.SearchResult.RecordingResultWs2;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.ScoredId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class MusicBrainzWrapper {

    public List<ScoredId3Tag> search(Id3Tag tag) {
        List<RecordingResultWs2> results = searchRecordings(tag);

        List<ScoredId3Tag> tags = new ArrayList<>();
        for(RecordingResultWs2 recordingResultWs2 : results) {
            ScoredId3Tag resultTag = new ScoredId3Tag(build(recordingResultWs2), recordingResultWs2.getScore());
            tags.add(resultTag);
        }

        return tags;
    }

    private List<RecordingResultWs2> searchRecordings(Id3Tag tag) {
        Recording recording = new Recording();
        RecordingSearchFilterWs2 filter = recording.getSearchFilter();

        filter.setLimit(20L);
        filter.setQuery(tag.get(SupportedTag.TITLE)
                + " AND artist:" + tag.get(SupportedTag.ARTIST)
                + " tnum:" + tag.get(SupportedTag.TRACK)
                + " release:" + tag.get(SupportedTag.ALBUM));

        recording.search(recording.getSearchFilter().getQuery());

        //TODO: Crash if internet not work
        return recording.getFullSearchResultList();
    }

    private Id3Tag build(RecordingResultWs2 recordingResultWs2) {
        RecordingWs2 recordingWs2 = recordingResultWs2.getRecording();

        Id3Tag resultTag = new Id3Tag();
        resultTag.put(SupportedTag.TITLE, recordingWs2.getTitle());
        resultTag.put(SupportedTag.ARTIST, recordingWs2.getArtistCreditString());
        resultTag.put(SupportedTag.YEAR, recordingWs2.getReleases().get(0).getYear());
        resultTag.put(SupportedTag.ALBUM, recordingWs2.getReleases().get(0).getTitle());
        resultTag.put(SupportedTag.TRACK, ""+ recordingWs2.getReleases().get(0).getMediumList().getMedia().get(0).getPosition());

        return resultTag;
    }


}
