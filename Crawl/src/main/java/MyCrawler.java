import Domain.Crawling;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.Header;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {

    public static DBConnect db = new DBConnect();

    private static final Pattern FILTER = Pattern.compile(".*(\\.(css|js|gif|jpg"
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

        logger.info("URL: {}", webURL.getURL());
        logger.info("Parent URL: {}", webURL.getParentUrl());

        Crawling crawling = new Crawling();
        crawling.setUrl(webURL.getURL());
        crawling.setDomain(webURL.getDomain());
        crawling.setLevel(webURL.getDepth());
        crawling.setParent_url(webURL.getParentUrl());

        try {
            db.insertData(crawling);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            logger.info("Text length: {}", text.length());
            logger.info("Html length: {}", html.length());
            logger.info("Number of outgoing links: {}", links.size());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            logger.debug("Response headers:");
            for (Header header : responseHeaders) {
                logger.debug("\t{}: {}", header.getName(), header.getValue());
            }
        }

        logger.debug("=============");
    }
}