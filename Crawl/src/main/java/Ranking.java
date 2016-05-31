import Domain.Crawling;

/**
 * Created by Hakim on 27-May-16.
 */
public interface Ranking {
    Double inLinkScore(Crawling crawling);
    Double outLinkScore(Crawling crawling);
    void RankScore();
}
