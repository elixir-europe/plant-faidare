package fr.inrae.urgi.faidare.dao.v2;

import java.util.List;

public class StudyCriteria {

    private List<String> commonCropName;
    private List<String> externalReferenceID;
    private List<String> externalReferenceSource;
    private List<String> germplasmDbIds;
    private List<String> germplasmNames;
    private List<String> locationDbId;
    private List<String> locationName;
    private List<String> observationVariableDbId;
    private List<String> observationVariableName;
    private List<String> observationVariablePUI;
    private List<String> programDbId;
    private List<String> programName;
    private List<String> seasonDbId;
    private List<String> sortBy;
    private List<String> sortOrder;
    private List<String> studyCode;
    private List<String> studyDbId;
    private List<String> studyName;
    private List<String> studyPUI;
    private List<String> studyType;
    private List<String> trialDbIds;
    private List<String> trialNames;
    private Integer page;
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<String> getCommonCropName() { return commonCropName; }

    public void setCommonCropName(List<String> commonCropNames) { this.commonCropName = commonCropNames; }

    public List<String> getExternalReferenceID() { return externalReferenceID; }

    public void setExternalReferenceID(List<String> externalReferenceIDs) { this.externalReferenceID = externalReferenceIDs; }

    public List<String> getExternalReferenceSource() { return externalReferenceSource; }

    public void setExternalReferenceSource(List<String> externalReferenceSources) { this.externalReferenceSource = externalReferenceSources; }

    public List<String> getGermplasmDbIds() { return germplasmDbIds; }

    public void setGermplasmDbIds(List<String> germplasmDbIds) { this.germplasmDbIds = germplasmDbIds; }

    public List<String> getGermplasmNames() { return germplasmNames; }

    public void setGermplasmNames(List<String> germplasmNames) { this.germplasmNames = germplasmNames; }

    public List<String> getLocationDbId() { return locationDbId; }

    public void setLocationDbId(List<String> locationDbId) { this.locationDbId = locationDbId; }

    public List<String> getLocationName() { return locationName; }

    public void setLocationName(List<String> locationName) { this.locationName = locationName; }

    public List<String> getObservationVariableDbId() { return observationVariableDbId; }

    public void setObservationVariableDbId(List<String> observationVariableDbIds) { this.observationVariableDbId = observationVariableDbIds; }

    public List<String> getObservationVariableName() { return observationVariableName; }

    public void setObservationVariableName(List<String> observationVariableNames) { this.observationVariableName = observationVariableNames; }

    public List<String> getObservationVariablePUI() { return observationVariablePUI; }

    public void setObservationVariablePUI(List<String> observationVariablePUIs) { this.observationVariablePUI = observationVariablePUIs; }

    public List<String> getProgramDbId() { return programDbId; }

    public void setProgramDbId(List<String> programDbIds) { this.programDbId = programDbIds; }

    public List<String> getProgramName() { return programName; }

    public void setProgramName(List<String> programNames) { this.programName = programNames; }

    public List<String> getSeasonDbId() { return seasonDbId; }

    public void setSeasonDbId(List<String> seasonDbIds) { this.seasonDbId = seasonDbIds; }

    public List<String> getSortBy() { return sortBy; }

    public void setSortBy(List<String> sortBy) { this.sortBy = sortBy; }

    public List<String> getSortOrder() { return sortOrder; }

    public void setSortOrder(List<String> sortOrder) { this.sortOrder = sortOrder; }

    public List<String> getStudyCode() { return studyCode; }

    public void setStudyCode(List<String> studyCodes) { this.studyCode = studyCodes; }

    public List<String> getStudyDbId() { return studyDbId; }

    public void setStudyDbId(List<String> studyDbIds) { this.studyDbId = studyDbIds; }

    public List<String> getStudyName() { return studyName; }

    public void setStudyName(List<String> studyNames) { this.studyName = studyNames; }

    public List<String> getStudyPUI() { return studyPUI; }

    public void setStudyPUI(List<String> studyPUIs) { this.studyPUI = studyPUIs; }

    public List<String> getStudyType() { return studyType; }

    public void setStudyType(List<String> studyTypes) { this.studyType = studyTypes; }

    public List<String> getTrialDbIds() { return trialDbIds; }

    public void setTrialDbIds(List<String> trialDbIds) { this.trialDbIds = trialDbIds; }

    public List<String> getTrialNames() { return trialNames; }

    public void setTrialNames(List<String> trialNames) { this.trialNames = trialNames; }
}
