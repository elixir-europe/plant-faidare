package fr.inrae.urgi.faidare.dao.v2;

import java.util.List;

public class TrialCriteria {

    private List<String> trialDbId;
    private List<String> trialName;
    private List<String> trialType;
    private List<String> instituteName;
    private List<String> programDbId;
    private List<String> programNames;
    private List<String> studyDbIds;

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

    public List<String> getTrialType() {
        return trialType;
    }

    public void setTrialType(List<String> trialType) {
        this.trialType = trialType;
    }

    public List<String> getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(List<String> instituteName) {
        this.instituteName = instituteName;
    }

    public List<String> getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(List<String> programDbId) {
        this.programDbId = programDbId;
    }

    public List<String> getProgramNames() {
        return programNames;
    }

    public void setProgramNames(List<String> programNames) {
        this.programNames = programNames;
    }
    public List<String> getStudyDbIds() {
        return studyDbIds;
    }

    public void setStudyDbIds(List<String> studyDbIds) {
        this.studyDbIds = studyDbIds;
    }
}
