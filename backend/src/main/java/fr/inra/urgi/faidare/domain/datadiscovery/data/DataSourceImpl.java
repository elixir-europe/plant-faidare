package fr.inra.urgi.faidare.domain.datadiscovery.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * @author gcornut
 */
public class DataSourceImpl implements Serializable, DataSource {
    private final List<String> type = Collections.singletonList(
        "schema:DataCatalog"
    );
    private String uri;
    private String identifier;
    private String name;
    private String url;
    private String image;

    public DataSourceImpl() {}
    public DataSourceImpl(String uri, String identifier, String name, String url, String image) {
        this.uri = uri;
        this.identifier = identifier;
        this.name = name;
        this.url = url;
        this.image = image;
    }

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
    public String getImage() {
        return image;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
