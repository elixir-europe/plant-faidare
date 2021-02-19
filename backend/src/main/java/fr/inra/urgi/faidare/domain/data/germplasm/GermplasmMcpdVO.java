package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmMcpd;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Document(type = "germplasmMcpd")
public class GermplasmMcpdVO implements Serializable, BrapiGermplasmMcpd, ExtendedGermplasmMcpd {

    private Long groupId;

    @Id
    private String germplasmDbId;
    private String germplasmPUI;
    private String accessionNumber;
    private List<String> alternateIDs;
    private List<String> accessionNames;

    private String commonCropName;
    private String genus;
    private String species;
    private String speciesAuthority;
    private String subtaxon;
    private String subtaxonAuthority;

    private String ancestralData;
    private String biologicalStatusOfAccessionCode;
    private String mlsStatus;
    private String geneticNature;
    private String presenceStatus;
    private String remarks;

    private String countryOfOriginCode;
    private SiteVO originSite;

    private String instituteCode;
    private InstituteVO holdingInstitute;
    private InstituteVO holdingGenbank;

    private CollectingInfoVO collectingInfo;
    private String acquisitionDate;
    private String acquisitionSourceCode;

    private List<DonorInfoVO> donorInfo;
    private List<DistributorInfoVO> distributorInfo;


    @JsonDeserialize(contentAs = InstituteVO.class)
    private List<InstituteVO> breedingInstitutes;
    private String breederAccessionNumber;
    private String breedingCreationYear;
    private String catalogRegistrationYear;
    private String catalogDeregistrationYear;

    @JsonDeserialize(contentAs = InstituteVO.class)
    private List<InstituteVO> safetyDuplicateInstitutes;
    private List<String> storageTypeCodes;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmPUI() {
        return germplasmPUI;
    }

    public void setGermplasmPUI(String germplasmPUI) {
        this.germplasmPUI = germplasmPUI;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public List<String> getAlternateIDs() {
        return alternateIDs;
    }

    public void setAlternateIDs(List<String> alternateIDs) {
        this.alternateIDs = alternateIDs;
    }

    public List<String> getAccessionNames() {
        return accessionNames;
    }

    public void setAccessionNames(List<String> accessionNames) {
        this.accessionNames = accessionNames;
    }

    public String getCommonCropName() {
        return commonCropName;
    }

    public void setCommonCropName(String commonCropName) {
        this.commonCropName = commonCropName;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSpeciesAuthority() {
        return speciesAuthority;
    }

    public void setSpeciesAuthority(String speciesAuthority) {
        this.speciesAuthority = speciesAuthority;
    }

    public String getSubtaxon() {
        return subtaxon;
    }

    public void setSubtaxon(String subtaxon) {
        this.subtaxon = subtaxon;
    }

    public String getSubtaxonAuthority() {
        return subtaxonAuthority;
    }

    public void setSubtaxonAuthority(String subtaxonAuthority) {
        this.subtaxonAuthority = subtaxonAuthority;
    }

    public String getAncestralData() {
        return ancestralData;
    }

    public void setAncestralData(String ancestralData) {
        this.ancestralData = ancestralData;
    }

    public String getBiologicalStatusOfAccessionCode() {
        return biologicalStatusOfAccessionCode;
    }

    public void setBiologicalStatusOfAccessionCode(String biologicalStatusOfAccessionCode) {
        this.biologicalStatusOfAccessionCode = biologicalStatusOfAccessionCode;
    }

    public String getMlsStatus() {
        return mlsStatus;
    }

    public void setMlsStatus(String mlsStatus) {
        this.mlsStatus = mlsStatus;
    }

    public String getGeneticNature() {
        return geneticNature;
    }

    public void setGeneticNature(String geneticNature) {
        this.geneticNature = geneticNature;
    }

    public String getPresenceStatus() {
        return presenceStatus;
    }

    public void setPresenceStatus(String presenceStatus) {
        this.presenceStatus = presenceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCountryOfOriginCode() {
        return countryOfOriginCode;
    }

    public void setCountryOfOriginCode(String countryOfOriginCode) {
        this.countryOfOriginCode = countryOfOriginCode;
    }

    public SiteVO getOriginSite() {
        return originSite;
    }

    public void setOriginSite(SiteVO originSite) {
        this.originSite = originSite;
    }

    public String getInstituteCode() {
        return instituteCode;
    }

    public void setInstituteCode(String instituteCode) {
        this.instituteCode = instituteCode;
    }

    public InstituteVO getHoldingInstitute() {
        return holdingInstitute;
    }

    public void setHoldingInstitute(InstituteVO holdingInstitute) {
        this.holdingInstitute = holdingInstitute;
    }

    public InstituteVO getHoldingGenbank() {
        return holdingGenbank;
    }

    public void setHoldingGenbank(InstituteVO holdingGenbank) {
        this.holdingGenbank = holdingGenbank;
    }

    public CollectingInfoVO getCollectingInfo() {
        return collectingInfo;
    }

    public void setCollectingInfo(CollectingInfoVO collectingInfo) {
        this.collectingInfo = collectingInfo;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getAcquisitionSourceCode() {
        return acquisitionSourceCode;
    }

    public void setAcquisitionSourceCode(String acquisitionSourceCode) {
        this.acquisitionSourceCode = acquisitionSourceCode;
    }

    public List<DonorInfoVO> getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(List<DonorInfoVO> donorInfo) {
        this.donorInfo = donorInfo;
    }

    public List<DistributorInfoVO> getDistributorInfo() {
        return distributorInfo;
    }

    public void setDistributorInfo(List<DistributorInfoVO> distributorInfo) {
        this.distributorInfo = distributorInfo;
    }

    public List<InstituteVO> getBreedingInstitutes() {
        return breedingInstitutes;
    }

    public void setBreedingInstitutes(List<InstituteVO> breedingInstitutes) {
        this.breedingInstitutes = breedingInstitutes;
    }

    public String getBreederAccessionNumber() {
        return breederAccessionNumber;
    }

    public void setBreederAccessionNumber(String breederAccessionNumber) {
        this.breederAccessionNumber = breederAccessionNumber;
    }

    public String getBreedingCreationYear() {
        return breedingCreationYear;
    }

    public void setBreedingCreationYear(String breedingCreationYear) {
        this.breedingCreationYear = breedingCreationYear;
    }

    public String getCatalogRegistrationYear() {
        return catalogRegistrationYear;
    }

    public void setCatalogRegistrationYear(String catalogRegistrationYear) {
        this.catalogRegistrationYear = catalogRegistrationYear;
    }

    public String getCatalogDeregistrationYear() {
        return catalogDeregistrationYear;
    }

    public void setCatalogDeregistrationYear(String catalogDeregistrationYear) {
        this.catalogDeregistrationYear = catalogDeregistrationYear;
    }

    public List<InstituteVO> getSafetyDuplicateInstitutes() {
        return safetyDuplicateInstitutes;
    }

    public void setSafetyDuplicateInstitutes(List<InstituteVO> safetyDuplicateInstitutes) {
        this.safetyDuplicateInstitutes = safetyDuplicateInstitutes;
    }

    public List<String> getStorageTypeCodes() {
        return storageTypeCodes;
    }

    public void setStorageTypeCodes(List<String> storageTypeCodes) {
        this.storageTypeCodes = storageTypeCodes;
    }
}
