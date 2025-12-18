package fr.inrae.urgi.faidare.domain.variable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inrae.urgi.faidare.domain.jsonld.data.HasURI;
import fr.inrae.urgi.faidare.domain.jsonld.data.HasURL;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * @author gcornut
 */
public class OntologyVO implements BrapiOntology, HasURI, HasURL {
    private String ontologyName;
    private String ontologyDbId;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String version = "";

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String copyright = "";

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String licence = "";

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String authors = "";

    @JsonDeserialize(contentAs = OntologyLinkVO.class)
    private List<BrapiOntologyLink> links;

    // JSON-LD fields
    private String uri;
    private String url;

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getOntologyName() {
        return ontologyName;
    }

    public void setOntologyName(String ontologyName) {
        this.ontologyName = ontologyName;
    }

    @Override
    public String getOntologyDbId() {
        return ontologyDbId;
    }

    public void setOntologyDbId(String ontologyDbId) {
        this.ontologyDbId = ontologyDbId;
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Override
    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    @Override
    public List<BrapiOntologyLink> getLinks() {
        return links;
    }

    public void setLinks(List<BrapiOntologyLink> links) {
        this.links = links;
    }

    @Override
    public int hashCode() {
        return ontologyDbId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BrapiOntology && hashCode() == obj.hashCode();
    }

}
