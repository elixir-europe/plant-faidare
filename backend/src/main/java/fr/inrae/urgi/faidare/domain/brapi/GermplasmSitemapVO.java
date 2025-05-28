package fr.inrae.urgi.faidare.domain.brapi;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public final class GermplasmSitemapVO {

    @Id
    private String _id;
    private String germplasmDbId;

    public GermplasmSitemapVO() {
    }

    public GermplasmSitemapVO(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmSitemapVO that = (GermplasmSitemapVO) o;
        return Objects.equals(germplasmDbId, that.germplasmDbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(germplasmDbId);
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }
}

