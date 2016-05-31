import Domain.Crawling;
import Domain.Link;
import Domain.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakim on 27-May-16.
 */
public class Ranked implements Ranking{
    private DBConnect db = new DBConnect();
    private double dampingScore;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public Ranked(double dampingScore) {
        this.dampingScore = dampingScore;
    }

    public Double inLinkScore(Crawling crawling) {
        Double inLinkScore = 0.0;
        Double iStart = 0.0;
        Double iNext   = 0.0;
        List<Link> iNextList = new ArrayList<Link>();
        try {
            iStart = db.selectInLink(crawling.getId());
            ResultSet resultSet = db.selectByParentId(crawling.getParentId());
            while (resultSet.next()){
                iNextList.add(new Link(resultSet.getInt("url_id"), db.selectInLink(resultSet.getInt("url_id"))));
            }

            if(iNextList != null){
                for (Link link : iNextList){
                    iNext = iNext + link.getInlinkValue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inLinkScore = Double.valueOf(iStart / iNext);

        return inLinkScore;
    }

    public Double outLinkScore(Crawling crawling) {
        Double outLinkScore = 0.0;
        Double iStart = 0.0;
        Double iNext   = 0.0;
        List<Link> iNextList = new ArrayList<Link>();
        try {
            iStart = db.selectOutlink(crawling.getId());
            ResultSet resultSet = db.selectByParentId(crawling.getParentId());
            while (resultSet.next()){
                iNextList.add(new Link(resultSet.getInt("url_id"), db.selectOutlink(resultSet.getInt("url_id"))));
            }

            if(iNextList != null){
                for (Link link : iNextList){
                    iNext = iNext + link.getInlinkValue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        outLinkScore = Double.valueOf(iStart / iNext);

        return outLinkScore;
    }

    public void RankScore() {
        List<Crawling> crawlingList = new ArrayList<Crawling>();

        try {
            ResultSet resultSet = db.selectAllData();
            while (resultSet.next()){
                Crawling crawling = new Crawling();
                crawling.setId(resultSet.getInt("url_id"));
                crawling.setUrl(resultSet.getString("url"));
                crawling.setDomain(resultSet.getString("domain"));
                crawling.setLevel(resultSet.getInt("level"));
                crawling.setParentId(resultSet.getInt("parent_id"));
                crawling.setParentUrl(resultSet.getString("parent_url"));
                crawlingList.add(crawling);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(crawlingList != null){
            for (Crawling crawling : crawlingList){
                double rankScore = 0.0;
                double parentRank = 0.0;
                Rank rank = new Rank();
                rank.setUrlId(crawling.getId());
                rank.setLevel(crawling.getLevel());
                rank.setInLinkScore(inLinkScore(crawling));
                rank.setOutLinkScore(outLinkScore(crawling));
                rank.setParentId(crawling.getParentId());

                try {
                    ResultSet rankScoreList = db.selectRankScore(crawling.getParentId());
                    while (rankScoreList.next()){
                        if(Constants.DEBUG) logger.info("parent rank score" + rankScoreList.getDouble("rank_score"));
                        parentRank = rankScoreList.getDouble("rank_score");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(Constants.DEBUG){
                    logger.info("URL Id" + crawling.getId());
                    logger.info("parent value " + parentRank);
                    logger.info("d value " + dampingScore);
                    logger.info("inlink value " + rank.getInLinkScore());
                    logger.info("outlink value " + rank.getOutLinkScore());
                }

                rankScore = (1 - dampingScore) + dampingScore * parentRank * rank.getInLinkScore() * rank.getOutLinkScore();
                rank.setRankScore(rankScore);

                if(Constants.DEBUG) logger.info("Rank Score" + rankScore);

                try {
                    db.insertRank(rank);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
