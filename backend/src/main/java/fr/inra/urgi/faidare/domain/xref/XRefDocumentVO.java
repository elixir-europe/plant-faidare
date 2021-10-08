package fr.inra.urgi.faidare.domain.xref;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;

import java.util.List;

/**
 * Imported and adapted from unified-interface legacy
 * Model taken from transplant document fields
 *
 * @author fphilippe
 */
@Document(type = "xref")
public class XRefDocumentVO {

    @JsonProperty("groupId")
    private String groupId;

    @JsonProperty("entryType")
    private String entryType;

    @JsonProperty("databaseName")
    private String databaseName;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url")
    private String url;

    @JsonProperty("species")
    private String species;

    private List<String> linkedResourcesID;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public List<String> getLinkedResourcesID() {
        return linkedResourcesID;
    }

    public void setLinkedResourcesID(List<String> linkedResourcesID) {
        this.linkedResourcesID = linkedResourcesID;
    }

}
