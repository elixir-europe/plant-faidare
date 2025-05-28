package fr.inrae.urgi.faidare.domain;

public class GermplasmInstituteVO {

    private Integer accessionCreationDate;

    private Integer breedingCreationYear;

    private String accessionNumber;

    private String collectors;

    private Integer deregistrationYear;

    private String distributionStatus;

    private String germplasmPUI;

    private InstituteVO institute;

    private String materialType;

    private Integer registrationYear;


    public Integer getAccessionCreationDate() {
        return accessionCreationDate;
    }

    public void setAccessionCreationDate(Integer accessionCreationDate) {
        this.accessionCreationDate = accessionCreationDate;
    }

    public Integer getBreedingCreationYear() {
        return breedingCreationYear;
    }

    public void setBreedingCreationYear(Integer breedingCreationYear) {
        this.breedingCreationYear = breedingCreationYear;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getCollectors() {
        return collectors;
    }

    public void setCollectors(String collectors) {
        this.collectors = collectors;
    }

    public Integer getDeregistrationYear() {
        return deregistrationYear;
    }

    public void setDeregistrationYear(Integer deregistrationYear) {
        this.deregistrationYear = deregistrationYear;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public String getGermplasmPUI() {
        return germplasmPUI;
    }

    public void setGermplasmPUI(String germplasmPUI) {
        this.germplasmPUI = germplasmPUI;
    }

    public InstituteVO getInstitute() {
        return institute;
    }

    public void setInstitute(InstituteVO institute) {
        this.institute = institute;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public Integer getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(Integer registrationYear) {
        this.registrationYear = registrationYear;
    }
}
