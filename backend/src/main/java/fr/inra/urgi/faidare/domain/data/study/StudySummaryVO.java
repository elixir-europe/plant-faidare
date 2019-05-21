package fr.inra.urgi.faidare.domain.data.study;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiStudySummary;
import fr.inra.urgi.faidare.domain.data.GnpISInternal;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURI;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURL;
import fr.inra.urgi.faidare.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * StudyVO extending the official BreedingAPI specs with specific fields
 *
 * @author gcornut
 */
@Document(type = "study",
    // Fields for query and not display
    excludedFields = {"dataLinks", "contacts", "observationVariableDbIds", "germplasmDbIds"})
public class StudySummaryVO implements GnpISInternal, BrapiStudySummary, HasURI, HasURL, IncludedInDataCatalog {

    @Id
    private String studyDbId;

    private String studyName;
    private String studyType;
    private Boolean active;
    private List<String> seasons;
    private Date endDate;
    private Date startDate;

    private String programDbId;
    private String programName;

    private String trialDbId;
    private String trialName;
    private Set<String> trialDbIds;

    private BrapiAdditionalInfo additionalInfo;

    // Study summary only properties
    private String locationDbId;
    private String locationName;


    // Field only used to check mapping of criteria to this VO
    @JsonIgnore
    private List<String> observationVariableDbIds;

    @JsonIgnore
    private List<String> germplasmDbIds;

    // GnpIS specific fields
    private List<Long> speciesGroup;
    private Long groupId;

    // JSON-LD fields
    private String uri;
    private String url;
    private String sourceUri;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getSourceUri() {
        return this.sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @Override
    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    @Override
    public String getDocumentationURL() {
        return url;
    }

    public void setDocumentationURL(String documentationURL) {
        this.url = documentationURL;
    }

    @Override
    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    @Override
    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    @Override
    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    @Override
    public String getName() {
        return studyName;
    }

    public void setName(String name) {
        this.studyName = name;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    @Override
    public String getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    @Override
    public Set<String> getTrialDbIds() {
        return trialDbIds;
    }

    public void setTrialDbIds(Set<String> trialDbIds) {
        this.trialDbIds = trialDbIds;
    }

    @Override
    public BrapiAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(BrapiAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public List<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
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

    public List<String> getObservationVariableDbIds() {
        return observationVariableDbIds;
    }

    public void setObservationVariableDbIds(List<String> observationVariableDbIds) {
        this.observationVariableDbIds = observationVariableDbIds;
    }

    public List<String> getGermplasmDbIds() {
        return germplasmDbIds;
    }

    public void setGermplasmDbIds(List<String> germplasmDbIds) {
        this.germplasmDbIds = germplasmDbIds;
    }
}
