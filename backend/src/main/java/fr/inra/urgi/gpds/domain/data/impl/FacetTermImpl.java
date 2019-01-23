package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.data.FacetTerm;

/**
 * @author gcornut
 */
public class FacetTermImpl implements FacetTerm {
    private String term;
    private Long count;

    public FacetTermImpl(String term, Long count) {
        this.term = term;
        this.count = count;
    }

    public FacetTermImpl() {
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
