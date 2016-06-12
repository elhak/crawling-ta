package Preprocess;

import Connect.DBConnect;
import Domain.Token;
import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakim on 06-Jun-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class Stoplist {
    private DBConnect db = new DBConnect();

    public List<Token> checkToken(List<Token> tokenList){
        List<String> stopList = new ArrayList<String>();
        List<Token> newTokenList = new ArrayList<Token>();
        try {
            stopList = db.getStopList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!stopList.isEmpty()){
            for (int i = 0; i < tokenList.size(); i++) {
                Token tn = tokenList.get(i);
                if(!stopList.contains(tn.getContent())){
                    if(tn.getContent().length() > 1){
                        PorterStemmer ps = new PorterStemmer(tn.getContent());
                        ps.stem();
                        tn.setContent(ps.toString());
                        newTokenList.add(tn);
                    }
                }
            }
        }

        return newTokenList;
    }
}
