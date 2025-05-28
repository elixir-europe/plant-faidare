package fr.inrae.urgi.faidare.domain;

import java.util.List;
import java.util.Objects;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('xref', 0L)}",
    createIndex = false
)
public class XRefDocumentVO {

    @Id
    private String _id;
    private String groupId;
    private String entryType;
    private String databaseName;
    private String identifier;
    private String name;
    private String description;
    private String url;
    private List<String> species;
    private List<String> linkedResourcesID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XRefDocumentVO that = (XRefDocumentVO) o;
        return Objects.equals(_id, that._id) && Objects.equals(groupId, that.groupId) && Objects.equals(entryType, that.entryType) && Objects.equals(databaseName, that.databaseName) && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(url, that.url) && Objects.equals(species, that.species) && Objects.equals(linkedResourcesID, that.linkedResourcesID);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getLinkedResourcesID() {
        return linkedResourcesID;
    }

    public void setLinkedResourcesID(List<String> linkedResourcesID) {
        this.linkedResourcesID = linkedResourcesID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, groupId, entryType, databaseName, identifier, name, description, url, species, linkedResourcesID);
    }
}
