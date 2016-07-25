import Domain.Doc;

/**
 * Created by Hakim on 27-May-16.
 */
public interface Ranking {
    Double inLinkScore(Doc crawling);
    Double outLinkScore(Doc crawling);
    void RankScore();
}
