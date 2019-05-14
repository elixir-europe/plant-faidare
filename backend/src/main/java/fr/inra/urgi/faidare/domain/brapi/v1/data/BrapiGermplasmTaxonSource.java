package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiGermplasmTaxonSource {
    @JsonView(JSONView.BrapiFields.class)
    String getTaxonId();

    @JsonView(JSONView.BrapiFields.class)
    String getSourceName();
}
