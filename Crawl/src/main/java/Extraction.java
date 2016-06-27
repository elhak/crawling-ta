import Connect.DBConnect;
import Domain.FreqCount;
import Domain.Token;
import Preprocess.Stoplist;
import Preprocess.Tokenizer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Hakim on 06-Jun-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class Extraction{
    private DBConnect db = new DBConnect();

    private Stoplist stoplist = new Stoplist();

    public void start(){
        try {
            int total = db.getTotalContent();
            for (int i = 0; i < total;) {
                ResultSet rs = db.getContent(5, i);
                while (rs.next()){
                    Ekstrak(rs.getInt("id"), rs.getString("content"));
                }

                i = i + 5;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Ekstrak(int id, String input) throws SQLException {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokenList = tokenizer.Token(input);

        if(!tokenList.isEmpty()){
            List<Token> out = stoplist.checkToken(tokenList);
            int i = 0;
            for (Token token : out){
                boolean isTokenExist = false;
                ResultSet termSet = db.checkTerm(token.getContent());
                while (termSet.next()){
                    token.setId(termSet.getInt("id"));
                    isTokenExist = true;
                }

                if(!isTokenExist){
                    token.setId(db.insertTerm(token));
                }
            }

            ResultSet urlList = db.getUrlByContent(id);
            while (urlList.next()){
                for (Token tok : out){
                    FreqCount freqCount = new FreqCount();
                    freqCount.setUrlId(urlList.getInt("id"));
                    freqCount.setTermId(tok.getId());
                    freqCount.setTf(tok.getStringFreq());

                    if(!db.checkTf(freqCount)){
                        db.insertTf(freqCount);
                    }
                }
            }

            db.updateStatus(id);
        }
    }
}
