import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

    public static void main(String[] args) throws Exception {
        /*String crawlStorageFolder = "data/root";

        int numberOfCrawlers = 2;

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);

        config.setIncludeBinaryContentInCrawling(false);

        config.setResumableCrawling(true);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed("https://en.wikipedia.org/wiki/PageRank");

        controller.start(MyCrawler.class, numberOfCrawlers);*/

        /*Extraction extraction = new Extraction();
        extraction.start();*/

        /*Ranked ranked = new Ranked(0.85);
        ranked.RankScore();*/

        FrequencyCount frequencyCount = new FrequencyCount();
        frequencyCount.startCounting();
    }
}