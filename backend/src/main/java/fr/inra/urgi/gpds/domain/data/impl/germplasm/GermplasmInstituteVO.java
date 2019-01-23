package fr.inra.urgi.gpds.domain.data.impl.germplasm;

import fr.inra.urgi.gpds.domain.data.GermplasmInstitute;

import java.io.Serializable;

/**
 * @author C. Michotey
 */
public class GermplasmInstituteVO implements Serializable, GermplasmInstitute {

    private static final long serialVersionUID = 7976964107440923573L;

    private InstituteVO institute;
    private String germplasmPUI;
    private String accessionNumber;
    private Integer accessionCreationDate;
    private String materialType;
    private String collectors;
    private Integer registrationYear;
    private Integer deregistrationYear;
    private String distributionStatus;

    @Override
    public InstituteVO getInstitute() {
        return institute;
    }

    public void setInstitute(InstituteVO institute) {
        this.institute = institute;
    }

    @Override
    public String getGermplasmPUI() {
        return germplasmPUI;
    }

    public void setGermplasmPUI(String germplasmPUI) {
        this.germplasmPUI = germplasmPUI;
    }

    @Override
    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    @Override
    public Integer getAccessionCreationDate() {
        return accessionCreationDate;
    }

    public void setAccessionCreationDate(Integer accessionCreationDate) {
        this.accessionCreationDate = accessionCreationDate;
    }

    @Override
    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    @Override
    public String getCollectors() {
        return collectors;
    }

    public void setCollectors(String collectors) {
        this.collectors = collectors;
    }

    @Override
    public Integer getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(Integer registrationYear) {
        this.registrationYear = registrationYear;
    }

    @Override
    public Integer getDeregistrationYear() {
        return deregistrationYear;
    }

    public void setDeregistrationYear(Integer deregistrationYear) {
        this.deregistrationYear = deregistrationYear;
    }

    @Override
    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

}
