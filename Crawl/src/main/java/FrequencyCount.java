import Connect.DBConnect;
import Domain.Crawling;
import Domain.FreqCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Hakim on 08-Jun-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class FrequencyCount {
    private DBConnect dbConnect = new DBConnect();
    private Logger logger = LoggerFactory.getLogger(FrequencyCount.class);

    public void startCounting(){
        try {
            int nDoc = dbConnect.getDocumentTotal();
            int offset = 0;
            boolean finish = false;

            while (!finish){
                ResultSet rs = dbConnect.getFtDocument(offset);
                if(!rs.next()){
                    finish = true;
                }else{
                    do{
                        Crawling crawling = new Crawling();
                        crawling.setId(rs.getInt("url_id"));
                        int termId = rs.getInt("term_id");

                        int idf = dbConnect.inverseFrequencyCounting(termId);
                        FreqCount freqCount = new FreqCount();
                        freqCount.setUrlId(crawling.getId());
                        freqCount.setTermId(termId);
                        freqCount.setIdf(idf);
                        freqCount.setTf(rs.getInt("tf"));
                        freqCount.setWeight(calculateTfIdfFrequency(freqCount, nDoc));

                        logger.info("URL" + freqCount.getUrlId());
                        logger.info("Term " + freqCount.getTermId());
                        logger.info("Weigh " + freqCount.getWeight());
                        logger.info("TF " + freqCount.getTf());
                        logger.info("IDF " + freqCount.getIdf());

                        dbConnect.updateWeight(freqCount);
                    } while (rs.next());

                    offset = offset + 10;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private float calculateTfIdfFrequency(FreqCount fq, int nDoc){
       Float weight = (float) (fq.getTf() * (Math.log10(nDoc/fq.getIdf())+1));
        return weight;
    }
}
