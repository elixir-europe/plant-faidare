package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.domain.SynonymsVO;

import java.util.List;

public class GermplasmV2Criteria {

    private List<String> accessionNumber;
    private List<String> binomialNames;
    private List<String> collections;
    private List<String> commonCropNames;
    private List<String> externalReferenceIDs;
    private List<String> externalReferenceIds;
    private List<String> externalReferenceSources;
    private List<String> familyCodes;
    private List<String> genus;
    private List<String> germplasmDbId;
    private List<String> germplasmName;
    private List<String> germplasmPUIs;
    private List<String> instituteCodes;
    private Integer page;
    private Integer pageSize;
    private List<String> parentDbIds;
    private List<String>  progenyDbIds;
    private List<String> programDbIds;
    private List<String> programNames;
    private List<String> species;
    private List<String> studyDbIds;
    private List<String> studyNames;
    private List<SynonymsVO> synonyms;
    private List<String> trialDbIds;
    private List<String> trialNames;


    public List<String> getAccessionNumber() { return accessionNumber; }

    public void setAccessionNumber(List<String> accessionNumber) { this.accessionNumber = accessionNumber; }

    public List<String> getBinomialNames() { return binomialNames; }

    public void setBinomialNames(List<String> binomialNames) { this.binomialNames = binomialNames; }

    public List<String> getCollections() { return collections; }

    public void setCollections(List<String> collections) { this.collections = collections; }

    public List<String> getCommonCropNames() { return commonCropNames; }

    public void setCommonCropNames(List<String> commonCropNames) { this.commonCropNames = commonCropNames; }

    public List<String> getExternalReferenceIDs() { return externalReferenceIDs; }

    public void setExternalReferenceIDs(List<String> externalReferenceIDs) { this.externalReferenceIDs = externalReferenceIDs; }

    public List<String> getExternalReferenceIds() { return externalReferenceIds; }

    public void setExternalReferenceIds(List<String> externalReferenceIds) { this.externalReferenceIds = externalReferenceIds; }

    public List<String> getExternalReferenceSources() { return externalReferenceSources; }

    public void setExternalReferenceSources(List<String> externalReferenceSources) { this.externalReferenceSources = externalReferenceSources; }

    public List<String> getFamilyCodes() { return familyCodes; }

    public void setFamilyCodes(List<String> familyCodes) { this.familyCodes = familyCodes; }

    public List<String> getGenus() { return genus; }

    public void setGenus(List<String> genus) { this.genus = genus; }

    public List<String> getGermplasmDbId() { return germplasmDbId; }

    public void setGermplasmDbId(List<String> germplasmDbId) { this.germplasmDbId = germplasmDbId; }

    public List<String> getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(List<String> germplasmName) {
        this.germplasmName = germplasmName;
    }

    public List<String> getGermplasmPUIs() { return germplasmPUIs; }

    public void setGermplasmPUIs(List<String> germplasmPUIs) { this.germplasmPUIs = germplasmPUIs; }

    public List<String> getInstituteCodes() { return instituteCodes; }

    public void setInstituteCodes(List<String> instituteCodes) { this.instituteCodes = instituteCodes; }

    public Integer getPage() { return page; }

    public void setPage(Integer page) { this.page = page; }

    public Integer getPageSize() { return pageSize; }

    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    public List<String> getParentDbIds() { return parentDbIds; }

    public void setParentDbIds(List<String> parentDbIds) { this.parentDbIds = parentDbIds; }

    public List<String> getProgenyDbIds() { return progenyDbIds; }

    public void setProgenyDbIds(List<String> progenyDbIds) { this.progenyDbIds = progenyDbIds; }

    public List<String> getProgramDbIds() { return programDbIds; }

    public void setProgramDbIds(List<String> programDbIds) { this.programDbIds = programDbIds; }

    public List<String> getProgramNames() { return programNames; }

    public void setProgramNames(List<String> programNames) { this.programNames = programNames; }

    public List<String> getSpecies() { return species; }

    public void setSpecies(List<String> species) { this.species = species; }

    public List<String> getStudyDbIds() { return studyDbIds; }

    public void setStudyDbIds(List<String> studyDbIds) { this.studyDbIds = studyDbIds; }

    public List<String> getStudyNames() { return studyNames; }

    public void setStudyNames(List<String> studyNames) { this.studyNames = studyNames; }

    public List<SynonymsVO> getSynonyms() { return synonyms; }

    public void setSynonyms(List<SynonymsVO> synonyms) { this.synonyms = synonyms; }

    public List<String> getTrialDbIds() { return trialDbIds; }

    public void setTrialDbIds(List<String> trialDbIds) { this.trialDbIds = trialDbIds; }

    public List<String> getTrialNames() { return trialNames; }

    public void setTrialNames(List<String> trialNames) { this.trialNames = trialNames; }

}
