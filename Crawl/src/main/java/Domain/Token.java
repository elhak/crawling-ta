package Domain;

/**
 * Created by Hakim on 06-Jun-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class Token {
    private int id;
    private String content;
    private int stringFreq;

    public Token() {
    }

    public Token(String content, int stringFreq) {
        this.content = content;
        this.stringFreq = stringFreq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStringFreq() {
        return stringFreq;
    }

    public void setStringFreq(int stringFreq) {
        this.stringFreq = stringFreq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
