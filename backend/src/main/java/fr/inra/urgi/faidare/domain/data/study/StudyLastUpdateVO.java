package fr.inra.urgi.faidare.domain.data.study;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiStudyLastUpdate;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
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
