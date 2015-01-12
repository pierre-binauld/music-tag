package binauld.pierre.musictag.tag;

public class ScoredId3Tag {
    private Id3Tag tag;
    private int score;

    public ScoredId3Tag(Id3Tag tag, int score) {
        this.tag = tag;
        this.score = score;
    }

    public Id3Tag getTag() {
        return tag;
    }

    public void setTag(Id3Tag tag) {
        this.tag = tag;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
