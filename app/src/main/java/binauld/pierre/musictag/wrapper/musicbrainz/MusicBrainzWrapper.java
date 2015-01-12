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

/**
 * Wrap the MusicBrainz API for a better implementation in the app.
 */
public class MusicBrainzWrapper {

    private QueryBuilder queryBuilder = new QueryBuilder();

    /**
     * Search Id3Tag with its score from an Id3Tag.
     * @param tag The Id3Tag to search from.
     * @return Id3Tags found.
     */
    public List<ScoredId3Tag> search(Id3Tag tag) {
        List<RecordingResultWs2> results = searchRecordings(tag);

        List<ScoredId3Tag> tags = new ArrayList<>();
        for(RecordingResultWs2 recordingResultWs2 : results) {
            ScoredId3Tag resultTag = new ScoredId3Tag(build(recordingResultWs2), recordingResultWs2.getScore());
            tags.add(resultTag);
        }

        return tags;
    }

    /**
     * Search recordings from an Id3Tag.
     * @param tag The Id3Tag to search from.
     * @return Recordings found.
     */
    private List<RecordingResultWs2> searchRecordings(Id3Tag tag) {
        Recording recording = new Recording();
        RecordingSearchFilterWs2 filter = recording.getSearchFilter();

        filter.setLimit(20L);
        filter.setQuery(queryBuilder.build(tag));

        recording.search(recording.getSearchFilter().getQuery());

        //TODO: Crash if internet not work
        return recording.getFirstSearchResultPage();
    }

    /**
     * Build an Id3Tag from a RecordingResultWs2.
     * @param recordingResultWs2 The RecordingResultWs2 to build from.
     * @return The Id3Tag built.
     */
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
