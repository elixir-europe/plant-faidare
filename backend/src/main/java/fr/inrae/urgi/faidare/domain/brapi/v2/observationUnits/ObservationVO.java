package fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits;


import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('observation', 0L)}",
    createIndex = false
)
public class ObservationVO {
    private Long groupId; //GnpIS
    private String observationDbId;
    //private Map<String, Object> additionalInfo;
    private String collector;
    //private List<ExternalReferencesVO> externalReferences;
    //private Geometry geoCoordinates;
    private String germplasmDbId;
    private String germplasmName;
    private String observationTimeStamp; // ISO8601 format
    private Float gdd;
    private String observationUnitDbId;
    private String observationUnitName;
    private String observationVariableDbId;
    private String observationVariableName;
    private SeasonVO season;
    private String studyLocationDbId;
    private String studyName;
    private String studyDbId;
    private String studyLocation;
    private String trialDbId;
    private String trialName;
    //private String uploadedBy;
    private String value;
    //private Boolean isDataFile;
    @Id
    private String _id;

    public String getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getObservationDbId() {
        return observationDbId;
    }

    public void setObservationDbId(String observationDbId) {
        this.observationDbId = observationDbId;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }

    public String getObservationTimeStamp() {
        return observationTimeStamp;
    }

    public void setObservationTimeStamp(String observationTimeStamp) {
        this.observationTimeStamp = observationTimeStamp;
    }

    public Float getGdd() {
        return gdd;
    }

    public void setGdd(Float gdd) {
        this.gdd = gdd;
    }

    public String getObservationUnitDbId() {
        return observationUnitDbId;
    }

    public void setObservationUnitDbId(String observationUnitDbId) {
        this.observationUnitDbId = observationUnitDbId;
    }

    public String getObservationUnitName() {
        return observationUnitName;
    }

    public void setObservationUnitName(String observationUnitName) {
        this.observationUnitName = observationUnitName;
    }

    public String getObservationVariableDbId() {
        return observationVariableDbId;
    }

    public void setObservationVariableDbId(String observationVariableDbId) {
        this.observationVariableDbId = observationVariableDbId;
    }

    public String getObservationVariableName() {
        return observationVariableName;
    }

    public void setObservationVariableName(String observationVariableName) {
        this.observationVariableName = observationVariableName;
    }

    public SeasonVO getSeason() {
        return season;
    }

    public void setSeason(SeasonVO season) {
        this.season = season;
    }

    public String getStudyLocationDbId() {
        return studyLocationDbId;
    }

    public void setStudyLocationDbId(String studyLocationDbId) {
        this.studyLocationDbId = studyLocationDbId;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getStudyLocation() {
        return studyLocation;
    }

    public void setStudyLocation(String studyLocation) {
        this.studyLocation = studyLocation;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
