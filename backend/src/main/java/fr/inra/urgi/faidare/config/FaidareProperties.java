package fr.inra.urgi.faidare.config;

import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSourceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Properties class holding the properties of the application (typically stored in application.yml)
 *
 * @author gcornut
 */
@ConfigurationProperties(prefix = "faidare")
public class FaidareProperties {

    @NotBlank
    private String elasticsearchIndexingTemplate;

    @NotBlank
    private String elasticsearchXrefIndexName;

    @NotBlank
    private String cropOntologyRepositoryUrl;

    @NotBlank
    private String cropOntologyPortalLink;

    private String securityUserGroupWsUrl;
    private String securityUserGroupWsToken;

    /**
     * The URL used by the germplasm card to generate links to the faidare search application
     * (i.e. the faidare flavor of data-discovery).
     */
    @NotBlank
    private String searchUrl;

    private List<DataSourceImpl> dataSources = new ArrayList<>();

    public String getElasticsearchIndexingTemplate() {
        return elasticsearchIndexingTemplate;
    }

    public void setElasticsearchIndexingTemplate(String elasticsearchIndexingTemplate) {
        this.elasticsearchIndexingTemplate = elasticsearchIndexingTemplate;
    }

    public String getElasticsearchXrefIndexName() {
        return elasticsearchXrefIndexName;
    }

    public void setElasticsearchXrefIndexName(String elasticsearchXrefIndexName) {
        this.elasticsearchXrefIndexName = elasticsearchXrefIndexName;
    }

    public String getCropOntologyRepositoryUrl() {
        return cropOntologyRepositoryUrl;
    }

    public void setCropOntologyRepositoryUrl(String cropOntologyRepositoryUrl) {
        this.cropOntologyRepositoryUrl = cropOntologyRepositoryUrl;
    }

    public String getSecurityUserGroupWsUrl() {
        return securityUserGroupWsUrl;
    }

    public void setSecurityUserGroupWsUrl(String securityUserGroupWsUrl) {
        this.securityUserGroupWsUrl = securityUserGroupWsUrl;
    }

    public String getSecurityUserGroupWsToken() {
        return securityUserGroupWsToken;
    }

    public void setSecurityUserGroupWsToken(String securityUserGroupWsToken) {
        this.securityUserGroupWsToken = securityUserGroupWsToken;
    }

    public String getCropOntologyPortalLink() {
        return cropOntologyPortalLink;
    }

    public void setCropOntologyPortalLink(String cropOntologyPortalLink) {
        this.cropOntologyPortalLink = cropOntologyPortalLink;
    }

    public List<DataSourceImpl> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceImpl> dataSources) {
        this.dataSources = dataSources;
    }

    public DataSource getByUri(String uri) {
        for (DataSourceImpl dataSource : getDataSources()) {
            if (dataSource.getUri().equals(uri)) {
                return dataSource;
            }
        }
        return null;
    }

    /**
     * Get ElasticSearch alias name using the template property, the document type and the group id
     */
    public String getAliasName(String documentType, long groupId) {
        return getBaseIndexName(documentType) + "-group" + groupId;
    }

    /**
     * Get ElasticSearch index name using the template property, the document type and the index creation timestamp
     */
    public String getIndexName(String documentType, long startInstant) {
        return getBaseIndexName(documentType) + "-d" + startInstant;
    }

    public String getBaseIndexName(String documentType) {
        documentType = documentType.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase();
        return elasticsearchIndexingTemplate
            .replace("{documentType}", documentType.toLowerCase());
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }
}
