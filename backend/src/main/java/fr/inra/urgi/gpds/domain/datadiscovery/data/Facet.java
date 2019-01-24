package fr.inra.urgi.gpds.domain.datadiscovery.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

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
