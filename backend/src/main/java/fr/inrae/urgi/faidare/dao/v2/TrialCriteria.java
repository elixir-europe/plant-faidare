package fr.inrae.urgi.faidare.dao.v2;

import java.util.List;

public class TrialCriteria {

    private List<String> trialDbId;
    private List<String> trialName;
    private List<String> trialType;
    private List<String> active;
    private List<String> contactDbId;
    private List<String> trialPUI;
    private List<String> commonCropName;
    private List<String> instituteName;
    private List<String> programDbId;
    private List<String> programName;
    private List<String> studyDbId;
    private List<String> externalReferenceId;
    private List<String> externalReferenceSource;


    public List<String> getActive() {
        return active;
    }

    public void setActive(List<String> active) {
        this.active = active;
    }

    public List<String> getCommonCropName() {
        return commonCropName;
    }

    public void setCommonCropName(List<String> commonCropName) {
        this.commonCropName = commonCropName;
    }

    public List<String> getContactDbId() {
        return contactDbId;
    }

    public void setContactDbId(List<String> contactDbId) {
        this.contactDbId = contactDbId;
    }

    public List<String> getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(List<String> externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public List<String> getExternalReferenceSource() {
        return externalReferenceSource;
    }

    public void setExternalReferenceSource(List<String> externalReferenceSource) {
        this.externalReferenceSource = externalReferenceSource;
    }

    public List<String> getTrialPUI() {
        return trialPUI;
    }

    public void setTrialPUI(List<String> trialPUI) {
        this.trialPUI = trialPUI;
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

    public List<String> getProgramName() {
        return programName;
    }

    public void setProgramName(List<String> programName) {
        this.programName = programName;
    }
    public List<String> getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(List<String> studyDbId) {
        this.studyDbId = studyDbId;
    }
}
