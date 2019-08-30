package fr.inra.urgi.faidare.domain.data.germplasm;

public class DistributorInfoVO implements DistributorInfo {

    private String accessionNumber;
    private InstituteVO institute;
    private String distributionStatus;

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public InstituteVO getInstitute() {
        return institute;
    }

    public void setInstitute(InstituteVO institute) {
        this.institute = institute;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }



}
