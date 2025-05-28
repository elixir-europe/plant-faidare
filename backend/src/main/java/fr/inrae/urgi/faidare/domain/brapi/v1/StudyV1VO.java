package fr.inrae.urgi.faidare.domain.brapi.v1;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.*;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('study', 0L)}",
    createIndex = false
)
public final class StudyV1VO {

    //public static final String INDEX_NAME = "faidare_study_dev-group0";

    private boolean active;

    // private additionalInfo;

    private String commonCropName;

    private List<ContactVO> contacts;

    private String culturalPractices;

    private List<DataLinksVO> dataLinks;

    private String documentationURL;

    private LocalDate endDate;

    private List<EnvironmentParametersVO> environmentParameters;

    private PuiDescriptionVO experimentalDesign;

    private List<ExternalReferencesVO> externalReferences;

    private List<String> germplasmDbIds;

    private Long groupId;

    private PuiDescriptionVO growthFacility;

    private LastUpdateVO lastUpdate;

    private String license;

    private LocationVO location;

    private String locationDbId;

    private String locationName;

    private List<ObservationLevelsVO> ObservationLevels;

    private String observationUnitsDescription;

    private List<String> observationVariableDbIds;

    private String programDbId;

    private String programName;

    private List<String> seasons;

    @Field("schema:includedInDataCatalog")
    private String sourceUri;

    private LocalDate startDate;

    private String studyCode;

    @Id
    private String _id;
    private String studyDbId;
    private String studyDescription;
    private String studyName;
    private String studyPUI;
    private String studyType;
    private String trialDbId;
    private Set<String> trialsDbIds;
    private String trialName;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommonCropName() {
        return commonCropName;
    }

    public void setCommonCropName(String commonCropName) {
        this.commonCropName = commonCropName;
    }

    public List<ContactVO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactVO> contacts) {
        this.contacts = contacts;
    }

    public String getCulturalPractices() {
        return culturalPractices;
    }

    public void setCulturalPractices(String culturalPractices) {
        this.culturalPractices = culturalPractices;
    }

    public List<DataLinksVO> getDataLinks() {
        return dataLinks;
    }

    public void setDataLinks(List<DataLinksVO> dataLinks) {
        this.dataLinks = dataLinks;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<EnvironmentParametersVO> getEnvironmentParameters() {
        return environmentParameters;
    }

    public void setEnvironmentParameters(List<EnvironmentParametersVO> environmentParameters) { this.environmentParameters = environmentParameters; }

    public PuiDescriptionVO getExperimentalDesign() {
        return experimentalDesign;
    }

    public void setExperimentalDesign(PuiDescriptionVO experimentalDesign) { this.experimentalDesign = experimentalDesign; }

    public List<ExternalReferencesVO> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReferencesVO> externalReferences) { this.externalReferences = externalReferences; }

    public List<String> getGermplasmDbIds() {
        return germplasmDbIds;
    }

    public void setGermplasmDbIds(List<String> germplasmDbIds) {
        this.germplasmDbIds = germplasmDbIds;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public PuiDescriptionVO getGrowthFacility() {
        return growthFacility;
    }

    public void setGrowthFacility(PuiDescriptionVO growthFacility) {
        this.growthFacility = growthFacility;
    }

    public LastUpdateVO getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LastUpdateVO lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public LocationVO getLocation() {
        return location;
    }

    public void setLocation(LocationVO location) {
        this.location = location;
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<ObservationLevelsVO> getObservationLevels() {
        return ObservationLevels;
    }

    public void setObservationLevels(List<ObservationLevelsVO> observationLevels) { ObservationLevels = observationLevels; }

    public String getObservationUnitsDescription() {
        return observationUnitsDescription;
    }

    public void setObservationUnitsDescription(String observationUnitsDescription) { this.observationUnitsDescription = observationUnitsDescription; }

    public List<String> getObservationVariableDbIds() {
        return observationVariableDbIds;
    }

    public void setObservationVariableDbIds(List<String> observationVariableDbIds) { this.observationVariableDbIds = observationVariableDbIds; }

    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public List<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getStudyCode() {
        return studyCode;
    }

    public void setStudyCode(String studyCode) {
        this.studyCode = studyCode;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudyPUI() {
        return studyPUI;
    }

    public void setStudyPUI(String studyPUI) {
        this.studyPUI = studyPUI;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public Set<String> getTrialsDbIds() {
        return trialsDbIds;
    }

    public void setTrialsDbIds(Set<String> trialsDbIds) {
        this.trialsDbIds = trialsDbIds;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
