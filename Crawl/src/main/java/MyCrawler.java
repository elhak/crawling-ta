import Domain.Crawling;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {

    public static DBConnect db = new DBConnect();
    private List<Crawling> crawlingList;

    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("http://www.ics.uci.edu/");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        Crawling seed = new Crawling();
        seed.setDomain(domain);
        seed.setUrl(url);
        seed.setParentId(0);
        crawlingList.add(seed);

        try {
            seed.setId(db.insertData(seed));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        logger.info("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.info("Domain: '{}'", domain);
        logger.info("Sub-domain: '{}'", subDomain);
        logger.info("Path: '{}'", path);
        logger.info("Parent page: {}", parentUrl);
        logger.info("Anchor text: {}", anchor);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            for (WebURL webURL : links){
                logger.info("URL text: {}", webURL.getURL());
                Crawling crawling = new Crawling();
                crawling.setDomain(webURL.getDomain());
                crawling.setUrl(webURL.getURL());
                crawling.setParent_url(webURL.getParentUrl());
                crawling.setParentId(seed.getId());
                try {
                    crawling.setId(db.insertData(crawling));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                crawlingList.add(crawling);
            }
        }

        if(crawlingList != null){
            for (Crawling crawl : crawlingList){
                try {
                    db.insertMapper(crawl);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            logger.debug("Response headers:");
            for (Header header : responseHeaders) {
                logger.info("\t{}: {}", header.getName(), header.getValue());
            }
        }

        logger.debug("=============");
    }
}