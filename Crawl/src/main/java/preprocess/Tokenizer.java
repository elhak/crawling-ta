package preprocess;

import Domain.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Hakim on 06-Jun-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class Tokenizer {

    private String punctuation[] = {".", ",", ";", ":", "(", ")", "-", "/", "?", "!", "\"", "\'", "[", "]", "“", "”","‘", "{", "}"," " };

    public List<Token> Token(String output){
        List<Token> tokenList = new ArrayList<Token>();
        StringTokenizer stringTokenizer = new StringTokenizer(output.toLowerCase());
        while(stringTokenizer.hasMoreTokens()){
            String newString = stringTokenizer.nextToken();
            while((output.length() > 0) && startsWithPunctuation(newString)){
                newString = newString.substring(1);
            }
            while ((output.length() > 0) && endsWithPunctuation(newString)){
                newString = newString.substring(0, newString.length()-1);
            }
            newString = newString.trim();
            if((output.length() > 0)){
                tokenList.add(new Token(newString, 1));
            }
        }

        return tokenList;
    }

    private boolean startsWithPunctuation(String s){
        for (int i =0; i < punctuation.length; i++){
            if (s.startsWith(punctuation[i])){
                return true;
            }
        }
        return false;
    }

    private boolean endsWithPunctuation(String s){
        for (int i =0; i < punctuation.length; i++){
            if (s.endsWith(punctuation[i])){
                return true;
            }
        }
        return false;
    }

    private boolean isNumber(char charInput){
        return (charInput=='0'||charInput=='1'||charInput=='2'||charInput=='3'||charInput=='4'||charInput=='5'||charInput=='6'||charInput=='7'||charInput=='8'||charInput=='9');
    }

    private boolean isPunctuation(String s){
        int i=0;
        while(i < punctuation.length){
            if(s.equalsIgnoreCase(punctuation[i])){
                return true;
            }
            else{
                i++;
            }
        }
        return false;
    }
}
