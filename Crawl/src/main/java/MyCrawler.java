import Connect.Constants;
import Connect.DBConnect;
import Domain.Doc;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
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

        // Only accept the url if it is in the "https://en.wikipedia.org" domain and protocol is "https".
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
        boolean isContentExist = false;
        String title = "";
        String output = "";

        logger.info("URL: {}", webURL.getURL());
        logger.info("Parent URL: {}", webURL.getParentUrl());

        Doc crawling = new Doc();
        crawling.setUrl(webURL.getURL());
        crawling.setDomain(webURL.getDomain());
        crawling.setLevel(webURL.getDepth());
        crawling.setParentUrl(webURL.getParentUrl());

        if(page.getParseData() instanceof HtmlParseData){
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            int outLink = 0;
            Set<WebURL> urlList = htmlParseData.getOutgoingUrls();

            for (Iterator<WebURL> urls = urlList.iterator(); urls.hasNext(); ) {
                WebURL url = urls.next();
                String href = url.getURL().toLowerCase();

                if (!FILTER.matcher(href).matches()) {
                    if(!url.getURL().contains("edit")){
                        outLink++;
                    }
                }
            }

            title = htmlParseData.getTitle();
            output = text.replaceAll("[^a-zA-Z ]", " ");
            output = Jsoup.clean(output, Whitelist.basic());
            crawling.setTitle(title);
            crawling.setOutLinkSize(outLink);

            if(Constants.DEBUG) logger.info(MyCrawler.class + "Output " + output);
        }

        try {
            if(output.length() > 0){
                ResultSet rc = db.checkContent(output);
                while (rc.next()){
                    crawling.setContentId(rc.getInt("id"));
                    isContentExist = true;
                }

                if(!isContentExist){
                    crawling.setContentId(db.insertContent(output));
                }

                ResultSet urlSet = db.checkUrl(crawling.getUrl());
                while (urlSet.next()){
                    crawling.setId(urlSet.getInt("id"));
                    isUrlExist = true;
                }

                if(!isUrlExist){
                    crawling.setId(db.insertData(crawling));
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}