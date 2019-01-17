package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiGermplasmTaxonSource {
	@JsonView(JSONView.BrapiFields.class)
	String getTaxonId();

	@JsonView(JSONView.BrapiFields.class)
	String getSourceName();
}
