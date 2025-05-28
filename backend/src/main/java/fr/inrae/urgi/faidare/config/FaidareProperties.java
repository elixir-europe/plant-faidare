package fr.inrae.urgi.faidare.config;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties class holding the properties of the application (typically stored in application.yml)
 *
 * @author gcornut
 */
@ConfigurationProperties(prefix = "faidare")
public class FaidareProperties {
    private String securityUserGroupWsUrl;
    private String securityUserGroupWsToken;

    /**
     * The URL used by the germplasm card to generate links to the faidare search application
     * (i.e. the faidare flavor of data-discovery).
     */

    @NotBlank
    private String elasticsearchIndexingTemplate;

    @NotBlank
    private String searchUrl;

    @NotBlank
    private String cropOntologyRepositoryUrl;

    @NotBlank
    private String cropOntologyPortalLink;

    private List<DataSource> dataSources = new ArrayList<>();

    public void setSecurityUserGroupWsUrl(String securityUserGroupWsUrl) {
        this.securityUserGroupWsUrl = securityUserGroupWsUrl;
    }

    public String getSecurityUserGroupWsUrl() {
        return securityUserGroupWsUrl;
    }

    public String getSecurityUserGroupWsToken() {
        return securityUserGroupWsToken;
    }

    public void setSecurityUserGroupWsToken(String securityUserGroupWsToken) {
        this.securityUserGroupWsToken = securityUserGroupWsToken;
    }

    public String getElasticsearchIndexingTemplate() {
        return elasticsearchIndexingTemplate;
    }

    public void setElasticsearchIndexingTemplate(String elasticsearchIndexingTemplate) {
        this.elasticsearchIndexingTemplate = elasticsearchIndexingTemplate;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getCropOntologyRepositoryUrl() {
        return cropOntologyRepositoryUrl;
    }

    public void setCropOntologyRepositoryUrl(String cropOntologyRepositoryUrl) {
        this.cropOntologyRepositoryUrl = cropOntologyRepositoryUrl;
    }

    public String getCropOntologyPortalLink() {
        return cropOntologyPortalLink;
    }

    public void setCropOntologyPortalLink(String cropOntologyPortalLink) {
        this.cropOntologyPortalLink = cropOntologyPortalLink;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public DataSource getByUri(String uri) {
        for (DataSource dataSource : getDataSources()) {
            if (dataSource.getUri().equals(uri)) {
                return dataSource;
            }
        }
        return null;
    }

    /**
     * Get Elasticearch alias name using the template property, the document type and the group id
     */
    public String getAliasName (String documentType, long groupId) {
        return getBaseIndexName(documentType) + "-group" + groupId;
    }


    private String getBaseIndexName(String documentType) {
        documentType = documentType.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase();
        return elasticsearchIndexingTemplate.replace("{documentType}", documentType);
    }
}
