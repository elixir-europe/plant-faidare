package fr.inrae.urgi.faidare.domain.brapi.v1;

import java.util.Objects;

public class SiblingV1VO {

    private String germplasmDbId;
    private String defaultDisplayName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiblingV1VO that = (SiblingV1VO) o;
        return Objects.equals(germplasmDbId, that.germplasmDbId) && Objects.equals(defaultDisplayName, that.defaultDisplayName);
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(germplasmDbId, defaultDisplayName);
    }
}
