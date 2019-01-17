package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 *
 *
 */
public interface FacetTerm {

	@JsonView(JSONView.GnpISFields.class)
	String getTerm();

	@JsonView(JSONView.GnpISFields.class)
	Long getCount();

}
