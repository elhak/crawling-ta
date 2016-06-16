import Connect.DBConnect;
import Domain.Crawling;
import Domain.FreqCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            ResultSet rs = dbConnect.selectAll();
            while (rs.next()){
                Crawling crawling = new Crawling();
                crawling.setId(rs.getInt("id"));
                crawling.setUrl(rs.getString("url"));

                ResultSet fq = dbConnect.frequencyCounting(crawling.getId());
                while (fq.next()){
                    int termId = fq.getInt("term_id");
                    if(!dbConnect.checkDataFreq(crawling.getId(), termId)){
                        int idf = dbConnect.inverseFrequencyCounting(termId);
                        FreqCount freqCount = new FreqCount();
                        freqCount.setUrlId(crawling.getId());
                        freqCount.setTermId(termId);
                        freqCount.setIdf(idf);
                        freqCount.setTf(fq.getInt("tf"));
                        freqCount.setWeight(calculateTfIdfFrequency(freqCount, nDoc));

                        logger.info("URL" + freqCount.getUrlId());
                        logger.info("Term " + freqCount.getTermId());
                        logger.info("Weigh " + freqCount.getWeight());
                        logger.info("TF " + freqCount.getTf());
                        logger.info("IDF " + freqCount.getIdf());

                        dbConnect.insertFrequency(freqCount);
                    }
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
