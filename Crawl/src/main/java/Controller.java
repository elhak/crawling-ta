import Domain.Crawling;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    public static DBConnect db = new DBConnect();

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public static void main(String[] args) throws Exception {

        System.out.print("Enter something:");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferRead.readLine();

        System.out.println(s);

        if(s.equals("1")){
            String crawlStorageFolder = "data/root";

            int numberOfCrawlers = 5;

            CrawlConfig config = new CrawlConfig();

            config.setCrawlStorageFolder(crawlStorageFolder);

            config.setPolitenessDelay(1000);

            config.setMaxDepthOfCrawling(3);

            config.setMaxPagesToFetch(-1);

            config.setIncludeBinaryContentInCrawling(false);

            config.setResumableCrawling(true);

            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            controller.addSeed("https://en.wikipedia.org/wiki/PageRank");
            controller.addSeed("https://en.wikipedia.org/wiki/HITS_algorithm");

            controller.start(MyCrawler.class, numberOfCrawlers);
        }else{
            mapper_link();
        }
    }

    public static void mapper_link() throws SQLException {
        ResultSet rs = db.selectAll();

        while (rs.next()){
            Crawling crawling = new Crawling();
            String parent_url = rs.getString("parent_url");
            crawling.setId(rs.getInt("id"));

            if(parent_url.equals("null")){
                crawling.setParentId(0);
            }else{
                ResultSet rss = db.selectParent(parent_url);
                while (rss.next()){
                    crawling.setParentId(rss.getInt("id"));
                }
            }

            db.insertMapper(crawling);
            logger.info(parent_url);
        }
    }
}