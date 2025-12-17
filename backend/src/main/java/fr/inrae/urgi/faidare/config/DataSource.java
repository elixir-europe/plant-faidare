package fr.inrae.urgi.faidare.config;

import java.util.List;


/**
 * @author gcornut
 */
public record DataSource(
    String uri,
    String name,
    String url,
    String image
) {

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
