package fr.inrae.urgi.faidare.domain.brapi;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public final class TrialSitemapVO {

    @Id
    private String _id;
    private String trialDbId;

    public TrialSitemapVO() {
    }

    public TrialSitemapVO(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrialSitemapVO that = (TrialSitemapVO) o;
        return Objects.equals(trialDbId, that.trialDbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trialDbId);
    }

    public String getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }
}

