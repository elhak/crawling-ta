import Connect.DBConnect;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Math.*;

public class Controller {

    public static void main(String[] args) throws Exception {

        DBConnect dbConnect = new DBConnect();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter something:");
        System.out.println("1. Crawler");
        System.out.println("2. Ekstraksi");
        System.out.println("3. PageRank");
        System.out.println("4. Freq Counting");
        System.out.println("5. Weight Cosine");
        int input = Integer.parseInt(br.readLine());

        switch (input){
            case 1 :
                String crawlStorageFolder = "data/root";

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

                controller.start(MyCrawler.class, numberOfCrawlers);
                break;
            case 2 :
                Extraction extraction = new Extraction();
                extraction.start();
                break;
            case 3 :
                Ranked ranked = new Ranked(0.85);
                ranked.RankScore();
                break;
            case 4 :
                FrequencyCount frequencyCount = new FrequencyCount();
                frequencyCount.startCounting();
                break;
            case 5 :
                SVDCalculation svdCalculation = new SVDCalculation(dbConnect);
                svdCalculation.calculate();
        }
    }
}