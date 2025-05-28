package fr.inrae.urgi.faidare.domain.brapi.v2;

import java.util.Objects;

public class PublicationsVO {

    private String publicationPUI;
    private String publicationReference;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationsVO that = (PublicationsVO) o;
        return Objects.equals(publicationPUI, that.publicationPUI) && Objects.equals(publicationReference, that.publicationReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationPUI, publicationReference);
    }

    public String getPublicationPUI() {
        return publicationPUI;
    }

    public void setPublicationPUI(String publicationPUI) {
        this.publicationPUI = publicationPUI;
    }

    public String getPublicationReference() {
        return publicationReference;
    }

    public void setPublicationReference(String publicationReference) {
        this.publicationReference = publicationReference;
    }
}
