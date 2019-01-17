package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;
import java.util.List;

/**
 * Properties used internally which should not be exposed
 *
 * @author gcornut
 *
 *
 */
public interface GnpISInternal extends Serializable {

	/**
	 * List of species group the VO belongs to
	 */
	@JsonView(JSONView.Internal.class)
	List<Long> getSpeciesGroup();

	/**
	 * Restricted group DB identifier from which the VO belong
	 */
	@JsonView(JSONView.Internal.class)
	Long getGroupId();

}
