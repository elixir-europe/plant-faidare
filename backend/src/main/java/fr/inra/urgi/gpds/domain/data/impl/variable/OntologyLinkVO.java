package fr.inra.urgi.gpds.domain.data.impl.variable;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiOntologyLink;

/**
 * Generic link reference inspired by HATEOS (used to list download links for the ontology)
 */
public class OntologyLinkVO implements BrapiOntologyLink {
    private String rel;
    private String href;

    @Override
    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    @Override
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
