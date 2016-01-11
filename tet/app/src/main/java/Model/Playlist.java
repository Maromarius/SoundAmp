package Model;

/**
 * Created by ramik on 1/11/2016.
 */
public class Playlist {

    private String coverArtURL;
    private String title;

    public Playlist(String coverArtURL, String title) {
        this.coverArtURL = coverArtURL;
        this.title = title;
    }

    public String getCoverArtURL() {
        return coverArtURL;
    }

    public void setCoverArtURL(String coverArtURL) {
        this.coverArtURL = coverArtURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
