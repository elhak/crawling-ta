package Domain;

/**
 * Created by Hakim on 29-May-16.
 */
public class Rank {
    private int urlId;
    private int level;
    private int parentId;
    private Double inLinkScore;
    private Double outLinkScore;
    private Double rankScore;

    public Rank() {
    }

    public int getUrlId() {
        return urlId;
    }

    public void setUrlId(int urlId) {
        this.urlId = urlId;
    }

    public Double getInLinkScore() {
        return inLinkScore;
    }

    public void setInLinkScore(Double inLinkScore) {
        this.inLinkScore = inLinkScore;
    }

    public Double getOutLinkScore() {
        return outLinkScore;
    }

    public void setOutLinkScore(Double outLinkScore) {
        this.outLinkScore = outLinkScore;
    }

    public Double getRankScore() {
        return rankScore;
    }

    public void setRankScore(Double rankScore) {
        this.rankScore = rankScore;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
