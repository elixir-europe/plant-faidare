package fr.inra.urgi.faidare.domain.data.study;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiStudyDataLink;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
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
