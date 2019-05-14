package fr.inra.urgi.faidare.domain.datadiscovery.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface FacetTerm {

    @JsonView(JSONView.GnpISFields.class)
    String getTerm();

    @JsonView(JSONView.GnpISFields.class)
    Long getCount();

}
