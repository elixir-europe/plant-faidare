package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * Base JSON-LD properties
 *
 * @author gcornut
 *
 *
 */
public interface JSONLD {

	/**
	 * rdf:type
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("@type")
	List<String> getType();

	/**
	 * URI
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("@id")
	String getUri();

}
