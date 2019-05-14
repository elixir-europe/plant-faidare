package fr.inra.urgi.faidare.domain.datadiscovery.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface Facet {

    @JsonView(JSONView.GnpISFields.class)
    String getField();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends FacetTerm> getTerms();

}
