package fr.inrae.urgi.faidare.config;

import java.util.List;


/**
 * @author gcornut
 */
public class DataSource {
    private String uri;
    private String name;
    private String url;
    private String image;

    public DataSource(String uri, String name, String url, String image) {
        this.uri = uri;
        this.name = name;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
}
