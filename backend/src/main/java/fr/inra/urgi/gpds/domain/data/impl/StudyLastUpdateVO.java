package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiStudyLastUpdate;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 *
 * @author gcornut
 *
 *
 */
public class StudyLastUpdateVO implements BrapiStudyLastUpdate {
	private String version;
	private String timestamp;

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
