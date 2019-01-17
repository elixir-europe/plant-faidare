package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationUnitTreatment;

/**
 * @author gcornut
 *
 *
 *
 */
public class TreatmentVO implements BrapiObservationUnitTreatment {

	private String factor;
	private String modality;

	@Override
	public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

	@Override
	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

}
