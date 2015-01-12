package binauld.pierre.musictag.tag;

/**
 * Contain an Id3Tag and its score for sorting.
 */
public class ScoredId3Tag {
    private Id3Tag tag;
    private int score;

    public ScoredId3Tag(Id3Tag tag, int score) {
        this.tag = tag;
        this.score = score;
    }

    /**
     * Get the Id3Tag.
     * @return The Id3Tag.
     */
    public Id3Tag getTag() {
        return tag;
    }

    /**
     * Set the Id3Tag.
     * @param tag The Id3Tag.
     */
    public void setTag(Id3Tag tag) {
        this.tag = tag;
    }

    /**
     * Get the score.
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the score.
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }
}
