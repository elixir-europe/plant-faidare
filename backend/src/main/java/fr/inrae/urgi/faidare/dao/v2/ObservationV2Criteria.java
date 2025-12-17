package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.SeasonVO;

import java.util.List;

public class ObservationV2Criteria {
    private List<String> observationDbId;
    private List<String> collector;
    private List<String> germplasmDbId;
    private List<String> germplasmName;
    private List<String> observationTimeStampRangeStart; // ISO8601 format
    private List<Float> gdd;
    private List<String> observationUnitDbId;
    private List<String> observationUnitName;
    private List<String> observationVariableDbId;
    private List<String> observationVariableName;
    private List<String> seasonDbId;
    private List<String> locationDbId;
    private List<String> studyDbId;
    private List<String> studyName;
    private List<String> locationName;
    private List<String> trialDbId;
    private List<String> trialName;
    private List<String> value;
    private Integer page;
    private Integer pageSize;

    public List<String> getObservationDbId() {
        return observationDbId;
    }

    public void setObservationDbId(List<String> observationDbId) {
        this.observationDbId = observationDbId;
    }

    public List<String> getCollector() {
        return collector;
    }

    public void setCollector(List<String> collector) {
        this.collector = collector;
    }

    public List<String> getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(List<String> germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public List<String> getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(List<String> germplasmName) {
        this.germplasmName = germplasmName;
    }

    public List<String> getObservationTimeStampRangeStart() {
        return observationTimeStampRangeStart;
    }

    public void setObservationTimeStampRangeStart(List<String> observationTimeStampRangeStart) {
        this.observationTimeStampRangeStart = observationTimeStampRangeStart;
    }

    public List<Float> getGdd() {
        return gdd;
    }

    public void setGdd(List<Float> gdd) {
        this.gdd = gdd;
    }

    public List<String> getObservationUnitDbId() {
        return observationUnitDbId;
    }

    public void setObservationUnitDbId(List<String> observationUnitDbId) {
        this.observationUnitDbId = observationUnitDbId;
    }

    public List<String> getObservationUnitName() {
        return observationUnitName;
    }

    public void setObservationUnitName(List<String> observationUnitName) {
        this.observationUnitName = observationUnitName;
    }

    public List<String> getObservationVariableDbId() {
        return observationVariableDbId;
    }

    public void setObservationVariableDbId(List<String> observationVariableDbId) {
        this.observationVariableDbId = observationVariableDbId;
    }

    public List<String> getObservationVariableName() {
        return observationVariableName;
    }

    public void setObservationVariableName(List<String> observationVariableName) {
        this.observationVariableName = observationVariableName;
    }

    public List<String> getSeasonDbId() {
        return seasonDbId;
    }

    public void setSeasonDbId(List<String> seasonDbId) {
        this.seasonDbId = seasonDbId;
    }

    public List<String> getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(List<String> locationDbId) {
        this.locationDbId = locationDbId;
    }

    public List<String> getStudyName() {
        return studyName;
    }

    public void setStudyName(List<String> studyName) {
        this.studyName = studyName;
    }

    public List<String> getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(List<String> studyDbId) {
        this.studyDbId = studyDbId;
    }

    public List<String> getLocationName() {
        return locationName;
    }

    public void setLocationName(List<String> locationName) {
        this.locationName = locationName;
    }

    public List<String> getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(List<String> trialDbId) {
        this.trialDbId = trialDbId;
    }

    public List<String> getTrialName() {
        return trialName;
    }

    public void setTrialName(List<String> trialName) {
        this.trialName = trialName;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
