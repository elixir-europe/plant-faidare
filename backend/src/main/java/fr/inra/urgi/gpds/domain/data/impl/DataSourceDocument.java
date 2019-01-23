package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.data.DataSource;

import java.io.Serializable;
import java.util.List;


/**
 * @author gcornut
 */
public class DataSourceDocument implements Serializable, DataSource {
    private String uri;
    private String identifier;
    private String name;
    private List<String> type;
    private String url;
    private String imageUrl;

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getType() {
        return type;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
