package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiTrialStudy;

/**
 * @author gcornut
 *
 *
 */
public class TrialStudySummaryVO implements BrapiTrialStudy {

	private String studyDbId;
	private String studyName;

	private String locationDbId;
	private String locationName;

	@Override
	public String getStudyDbId() {
		return studyDbId;
	}

	public void setStudyDbId(String studyDbId) {
		this.studyDbId = studyDbId;
	}

	@Override
	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	@Override
	public String getLocationDbId() {
		return locationDbId;
	}

	public void setLocationDbId(String locationDbId) {
		this.locationDbId = locationDbId;
	}

	@Override
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
}
