package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiStudyDataLink;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 *
 * @author gcornut
 *
 *
 */
public class StudyDataLinkVO implements BrapiStudyDataLink {
	private String name;
	private String type;
	private String url;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
