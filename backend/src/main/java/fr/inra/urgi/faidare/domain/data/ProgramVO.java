package fr.inra.urgi.faidare.domain.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiProgram;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURI;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURL;
import fr.inra.urgi.faidare.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

import java.util.List;

/**
 * ProgramVO extending the official BreedingAPI specs with specific fields
 *
 * @author gcornut
 */
@Document(type = "program")
public class ProgramVO implements GnpISInternal, BrapiProgram, HasURI, HasURL, IncludedInDataCatalog {

    @Id
    private String programDbId;
    private String programName;

    private String abbreviation;

    // This field is required to be non null in BRAVA
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String leadPerson = "";

    // This field is required to be non null in BRAVA
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String objective = "";

    // GnpIS specific fields
    private Long groupId;
    private List<Long> speciesGroup;

    // JSON-LD fields
    private String uri;
    private String url;
    private String sourceUri;

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
    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @Override
    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String getLeadPerson() {
        return leadPerson;
    }

    public void setLeadPerson(String leadPerson) {
        this.leadPerson = leadPerson;
    }

    @Override
    public String getName() {
        return programName;
    }

    public void setName(String name) {
        this.programName = name;
    }

    @Override
    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    @Override
    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    @Override
    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    @Override
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
