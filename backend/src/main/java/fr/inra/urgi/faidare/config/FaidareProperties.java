package fr.inra.urgi.faidare.config;

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
    private String elasticsearchAliasTemplate;

    @NotBlank
    private String elasticsearchXrefIndexName;

    @NotBlank
    private String cropOntologyRepositoryUrl;

    @NotBlank
    private String cropOntologyPortalLink;

    private String securityUserGroupWsUrl;
    private String securityUserGroupWsToken;

    private List<DataSourceImpl> dataSources = new ArrayList<>();

    public String getElasticsearchAliasTemplate() {
        return elasticsearchAliasTemplate;
    }

    public void setElasticsearchAliasTemplate(String elasticsearchAliasTemplate) {
        this.elasticsearchAliasTemplate = elasticsearchAliasTemplate;
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

    /**
     * Get ElasticSearch index name using the template property, the document type and the group id
     */
    public String getIndexName(String source, String documentType, long groupId) {
        return elasticsearchAliasTemplate
            .replace("{source}", source.toLowerCase())
            .replace("{documentType}", documentType.toLowerCase())
            .replace("{groupId}", String.valueOf(groupId));
    }

}
