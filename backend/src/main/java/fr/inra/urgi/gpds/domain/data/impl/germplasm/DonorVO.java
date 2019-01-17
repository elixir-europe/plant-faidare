package fr.inra.urgi.gpds.domain.data.impl.germplasm;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasmDonor;
import fr.inra.urgi.gpds.domain.data.ExtendedDonor;

import java.io.Serializable;

public class DonorVO implements Serializable, BrapiGermplasmDonor, ExtendedDonor {

	private static final long serialVersionUID = 7976964107440923573L;

	private String donorGermplasmPUI;
	private String donorAccessionNumber;
	private String donorInstituteCode;
	private Integer donationDate;
	private InstituteVO donorInstitute;

	@Override
	public String getDonorGermplasmPUI() {
		return donorGermplasmPUI;
	}

	public void setDonorGermplasmPUI(String donorGermplasmPUI) {
		this.donorGermplasmPUI = donorGermplasmPUI;
	}

	@Override
	public String getDonorAccessionNumber() {
		return donorAccessionNumber;
	}

	public void setDonorAccessionNumber(String donorAccessionNumber) {
		this.donorAccessionNumber = donorAccessionNumber;
	}

	@Override
	public String getDonorInstituteCode() {
		return donorInstituteCode;
	}

	public void setDonorInstituteCode(String donorInstituteCode) {
		this.donorInstituteCode = donorInstituteCode;
	}

	@Override
	public InstituteVO getDonorInstitute() {
		return donorInstitute;
	}

	public void setDonorInstitute(InstituteVO donorInstitute) {
		this.donorInstitute = donorInstitute;
	}

	@Override
	public Integer getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(Integer donationDate) {
		this.donationDate = donationDate;
	}

}
