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
    private Recording recording;
    private boolean firstPage;

    public void initQuery(Id3Tag id3Tag, long pageLimit) {
        firstPage = true;
        recording = new Recording();
        RecordingSearchFilterWs2 filter = recording.getSearchFilter();

        filter.setLimit(pageLimit);
        filter.setQuery(queryBuilder.build(id3Tag));

        recording.search(recording.getSearchFilter().getQuery());
    }

    /**
     * Search Id3Tag with its score from an Id3Tag.
     * @return Id3Tags found.
     */
    public List<ScoredId3Tag> nextSearchPage() {
        List<RecordingResultWs2> results;
        if(firstPage) {
            results = recording.getFirstSearchResultPage();
            firstPage = false;
        } else {
            results = recording.getNextSearchResultPage();
        }

        List<ScoredId3Tag> tags = new ArrayList<>();
        for(RecordingResultWs2 recordingResultWs2 : results) {
            ScoredId3Tag resultTag = new ScoredId3Tag(build(recordingResultWs2), recordingResultWs2.getScore());
            tags.add(resultTag);
        }

        return tags;
    }

    /**
     * Build an Id3Tag from a RecordingResultWs2.
     * @param recordingResultWs2 The RecordingResultWs2 to build from.
     * @return The Id3Tag built.
     */
    private Id3Tag build(RecordingResultWs2 recordingResultWs2) {
        RecordingWs2 recordingWs2 = recordingResultWs2.getRecording();
        Id3Tag resultTag = new Id3Tag();

        for(Map.Entry<SupportedTag, TagGetter> entry : TagGetter.getters.entrySet()) {
            resultTag.put(entry.getKey(), entry.getValue().get(recordingWs2));
        }

        return resultTag;
    }
}
