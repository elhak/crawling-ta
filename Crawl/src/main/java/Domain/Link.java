package Domain;

/**
 * Created by Hakim on 27-May-16.
 */
public class Link {
    private int urlId;
    private Double inlinkValue;

    public Link(int urlId, Double inlinkValue) {
        this.urlId = urlId;
        this.inlinkValue = inlinkValue;
    }

    public int getUrlId() {
        return urlId;
    }

    public void setUrlId(int urlId) {
        this.urlId = urlId;
    }

    public Double getInlinkValue() {
        return inlinkValue;
    }

    public void setInlinkValue(Double inlinkValue) {
        this.inlinkValue = inlinkValue;
    }
}
