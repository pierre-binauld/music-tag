package binauld.pierre.musictag.wrapper.musicbrainz;


import org.musicbrainz.Controller.Recording;
import org.musicbrainz.FilterWs2.SearchFilter.RecordingSearchFilterWs2;
import org.musicbrainz.modelWs2.Entity.RecordingWs2;
import org.musicbrainz.modelWs2.SearchResult.RecordingResultWs2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.tag.Id3Tag;
import binauld.pierre.musictag.tag.ScoredId3Tag;
import binauld.pierre.musictag.tag.SupportedTag;

public class MusicBrainzWrapper {

    private QueryBuilder queryBuilder = new QueryBuilder();

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
        filter.setQuery(buildQuery(tag));

        recording.search(recording.getSearchFilter().getQuery());

        //TODO: Crash if internet not work
        return recording.getFirstSearchResultPage();
    }

    private String buildQuery(Id3Tag id3Tag) {

        for(Map.Entry<SupportedTag, String> tag : id3Tag.entrySet()) {
            queryBuilder.append(tag.getKey(), tag.getValue());
        }

        return queryBuilder.toString();
    }

    private Id3Tag build(RecordingResultWs2 recordingResultWs2) {
        RecordingWs2 recordingWs2 = recordingResultWs2.getRecording();
        // Create a new Id3Tag assure to wipe out all tag.
        Id3Tag resultTag = new Id3Tag();

        for(Map.Entry<SupportedTag, TagGetter> entry : TagGetter.getters.entrySet()) {
            resultTag.put(entry.getKey(), entry.getValue().get(recordingWs2));
        }

        return resultTag;
    }



}
