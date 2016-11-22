import Connect.DBConnect;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Math.*;

public class Controller {

    public static void main(String[] args) throws Exception {

        DBConnect dbConnect = new DBConnect();

        SVDCalculation svdCalculation = new SVDCalculation(dbConnect);
        svdCalculation.calculate();

        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter something:");
        System.out.println("1. Crawler");
        System.out.println("2. Ekstraksi");
        System.out.println("3. PageRank");
        System.out.println("4. Freq Counting");
        System.out.println("5. Similarity");
        int input = Integer.parseInt(br.readLine());

        switch (input){
            case 1 :

                System.out.print("Enter Seed:");
                String seed = br.readLine();

                String crawlStorageFolder = "data/root";

                int numberOfCrawlers = 2;

                CrawlConfig config = new CrawlConfig();

                config.setCrawlStorageFolder(crawlStorageFolder);

                config.setMaxDepthOfCrawling(8);

                config.setIncludeBinaryContentInCrawling(false);

                config.setResumableCrawling(false);

                config.setMaxPagesToFetch(1000);

                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

                controller.addSeed(seed);

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
                break;
            case 6 :
                int[] i = new int[3];
                i = new int[]{10, 20, 30};
                Permutation pg = new Permutation(i, 1);
                while (pg.hasMore()) {
                    int[] temp =  pg.getNext();
                    for (int j = 0; j < temp.length; j++) {
                        System.out.print(temp[j] + " ");
                    }
                    System.out.println();
                }

                Set<Integer> mySet = new HashSet<Integer>();
                for (Set<Integer> s : pg.powerSet(mySet)) {
                    System.out.println(s);
                }
        }*/
    }
}