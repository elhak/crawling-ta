package Domain;

/**
 * Created by Hakim on 29-May-16.
 */
public class Rank {
    private int urlId;
    private int level;
    private int parentId;
    private double inLinkScore;
    private double outLinkScore;
    private double rankScore;

    public Rank() {
    }

    public int getUrlId() {
        return urlId;
    }

    public void setUrlId(int urlId) {
        this.urlId = urlId;
    }

    public double getInLinkScore() {
        return inLinkScore;
    }

    public void setInLinkScore(double inLinkScore) {
        this.inLinkScore = inLinkScore;
    }

    public double getOutLinkScore() {
        return outLinkScore;
    }

    public void setOutLinkScore(double outLinkScore) {
        this.outLinkScore = outLinkScore;
    }

    public double getRankScore() {
        return rankScore;
    }

    public void setRankScore(double rankScore) {
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
