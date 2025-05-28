package fr.inrae.urgi.faidare.domain;

public class DonorVO {

    private Integer donationDate; //GnpIS

    private String donorAccessionNumber;

    private String donorGermplasmPUI; //GnpIS

    private InstituteVO donorInstitute; //GnpIS

    private String donorInstituteCode;


    public Integer getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Integer donationDate) {
        this.donationDate = donationDate;
    }

    public String getDonorAccessionNumber() {
        return donorAccessionNumber;
    }

    public void setDonorAccessionNumber(String donorAccessionNumber) {
        this.donorAccessionNumber = donorAccessionNumber;
    }

    public String getDonorGermplasmPUI() {
        return donorGermplasmPUI;
    }

    public void setDonorGermplasmPUI(String donorGermplasmPUI) {
        this.donorGermplasmPUI = donorGermplasmPUI;
    }

    public InstituteVO getDonorInstitute() {
        return donorInstitute;
    }

    public void setDonorInstitute(InstituteVO donorInstitute) {
        this.donorInstitute = donorInstitute;
    }

    public String getDonorInstituteCode() {
        return donorInstituteCode;
    }

    public void setDonorInstituteCode(String donorInstituteCode) {
        this.donorInstituteCode = donorInstituteCode;
    }

}
