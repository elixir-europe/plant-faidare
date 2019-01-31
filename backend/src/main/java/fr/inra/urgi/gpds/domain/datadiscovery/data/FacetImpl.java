package fr.inra.urgi.gpds.domain.datadiscovery.data;

import java.util.List;

/**
 * @author gcornut
 */
public class FacetImpl implements Facet {
    private String field;
    private List<FacetTermImpl> terms;

    public FacetImpl(String field, List<FacetTermImpl> terms) {
        this.field = field;
        this.terms = terms;
    }

    public FacetImpl() {
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public List<FacetTermImpl> getTerms() {
        return terms;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setTerms(List<FacetTermImpl> terms) {
        this.terms = terms;
    }
}
