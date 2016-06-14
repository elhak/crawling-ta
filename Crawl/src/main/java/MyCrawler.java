import Connect.DBConnect;
import Domain.Crawling;
import Domain.Token;
import preprocess.Stoplist;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import preprocess.Tokenizer;
import javafx.scene.paint.Stop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {

    private DBConnect db = new DBConnect();

    private static final Pattern FILTER = Pattern.compile(".*(\\.(css|js|gif|jpg|jpeg"
                                                                        + "|png|mp3|mp3|zip|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (FILTER.matcher(href).matches()) {
            return false;
        }

        if(url.getURL().contains("edit")){
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("https://en.wikipedia.org");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        WebURL webURL = page.getWebURL();
        boolean isParentExist = false;
        boolean isUrlExist = false;
        String title = "";
        String output = "";

        logger.info("URL: {}", webURL.getURL());
        logger.info("Parent URL: {}", webURL.getParentUrl());

        Crawling crawling = new Crawling();
        crawling.setUrl(webURL.getURL());
        crawling.setDomain(webURL.getDomain());
        crawling.setLevel(webURL.getDepth());
        crawling.setParentUrl(webURL.getParentUrl());

        if(page.getParseData() instanceof HtmlParseData){
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            title = htmlParseData.getTitle();
            output = text.replaceAll("[^a-zA-Z ]", " ");
            crawling.setTitle(title);
        }

        try {

            ResultSet urlSet = db.checkUrl(crawling.getUrl());
            while (urlSet.next()){
                crawling.setId(urlSet.getInt("id"));
                isUrlExist = true;
            }

            if(!isUrlExist){
                crawling.setId(db.insertData(crawling));
                db.insertContent(crawling.getId(), output);
            }

            if(crawling.getLevel() > 0){
                ResultSet parentSet = db.checkParent(crawling.getParentUrl());
                while (parentSet.next()){
                    crawling.setParentId(parentSet.getInt("id"));
                    isParentExist = true;
                }

                if(!isParentExist){
                    crawling.setParentId(db.insertParentData(crawling));
                }

                if(!db.checkMapper(crawling)){
                    db.insertMapper(crawling);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}