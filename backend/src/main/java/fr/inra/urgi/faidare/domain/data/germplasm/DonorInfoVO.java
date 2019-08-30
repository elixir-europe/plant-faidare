package fr.inra.urgi.faidare.domain.data.germplasm;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmDonorInfo;

public class DonorInfoVO implements BrapiGermplasmDonorInfo, ExtendedDonorInfo {

    private String donorAccessionNumber;
    private InstituteVO donorInstitute;
    private String donationDate;

    @Override
    public String getDonorAccessionNumber() {
        return donorAccessionNumber;
    }

    @Override
    public InstituteVO getDonorInstitute() {
        return donorInstitute;
    }

    public void setDonorAccessionNumber(String donorAccessionNumber) {
        this.donorAccessionNumber = donorAccessionNumber;
    }

    public void setDonorInstitute(InstituteVO donorInstitute) {
        this.donorInstitute = donorInstitute;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }
}
