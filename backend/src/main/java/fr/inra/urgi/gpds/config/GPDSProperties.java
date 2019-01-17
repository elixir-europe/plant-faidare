package fr.inra.urgi.gpds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * Properties class holding the properties of the application (typically stored in application.yml)
 *
 * @author gcornut
 */
@ConfigurationProperties(prefix = "gpds")
public class GPDSProperties {

    @NotBlank
    private String elasticsearchAliasTemplate;

    @NotBlank
    private String cropOntologyRepositoryUrl;

    private String securityUserGroupWsUrl;

    private String securityUserGroupWsToken;

    public String getElasticsearchAliasTemplate() {
        return elasticsearchAliasTemplate;
    }

    public void setElasticsearchAliasTemplate(String elasticsearchAliasTemplate) {
        this.elasticsearchAliasTemplate = elasticsearchAliasTemplate;
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
