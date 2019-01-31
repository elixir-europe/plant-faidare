package fr.inra.urgi.gpds.domain.xref;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;

import java.util.List;

/**
 * Imported and adapted from unified-interface legacy
 * Model taken from transplant document fields
 *
 * @author fphilippe
 */
@Document(type = "transplant")
public class XRefDocumentVO {

    @JsonProperty("group_id")
    private String groupId;

    @JsonProperty("entry_type")
    private String entryType;

    @JsonProperty("database_name")
    private String databaseName;

    @JsonProperty("db_id")
    private String dbId;

    @JsonProperty("db_version")
    private String dbVersion;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url")
    private String url;

    @JsonProperty("species")
    private String species;

    @JsonProperty("xref")
    private String xref;

    @JsonProperty("feature_type")
    private String featureType;

    @JsonProperty("sequence_id")
    private String sequenceId;

    @JsonProperty("sequence_version")
    private String sequence_version;

    @JsonProperty("start_position")
    private String startPosition;

    @JsonProperty("end_position")
    private String endPosition;

    @JsonProperty("map")
    private String map;

    @JsonProperty("map_position")
    private String mapPosition;

    @JsonProperty("authority")
    private String authority;

    @JsonProperty("trait")
    private String trait;

    @JsonProperty("trait_id")
    private String traitId;

    @JsonProperty("environment")
    private String environment;

    @JsonProperty("environment_id")
    private String environmentId;

    @JsonProperty("statistic")
    private String statistic;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("genotype")
    private String genotype;

    @JsonProperty("experiment_type")
    private String experimentType;

    private List<String> linkedRessourcesID;

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

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
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

    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getSequence_version() {
        return sequence_version;
    }

    public void setSequence_version(String sequence_version) {
        this.sequence_version = sequence_version;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(String mapPosition) {
        this.mapPosition = mapPosition;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getTraitId() {
        return traitId;
    }

    public void setTraitId(String traitId) {
        this.traitId = traitId;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }

    public String getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType;
    }

    public List<String> getLinkedRessourcesID() {
        return linkedRessourcesID;
    }

    public void setLinkedRessourcesID(List<String> linkedRessourcesID) {
        this.linkedRessourcesID = linkedRessourcesID;
    }

}
