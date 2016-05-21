import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public static void main(String[] args) throws Exception {

            int numberOfCrawlers = 5;

            CrawlConfig config = new CrawlConfig();

            config.setCrawlStorageFolder("data");

            config.setPolitenessDelay(1000);

            config.setMaxDepthOfCrawling(3);

            config.setMaxPagesToFetch(1000);

            config.setIncludeBinaryContentInCrawling(false);
            config.setResumableCrawling(false);

            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            controller.addSeed("https://www.itb.ac.id/en/");
            controller.addSeed("https://en.wikipedia.org/wiki/PageRank");

            controller.start(MyCrawler.class, numberOfCrawlers);
    }
}