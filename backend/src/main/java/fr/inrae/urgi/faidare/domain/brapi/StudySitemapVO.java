package fr.inrae.urgi.faidare.domain.brapi;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public final class StudySitemapVO {

    @Id
    private String _id;
    private String studyDbId;

    public StudySitemapVO() {
    }

    public StudySitemapVO(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudySitemapVO that = (StudySitemapVO) o;
        return Objects.equals(studyDbId, that.studyDbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studyDbId);
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }
}

