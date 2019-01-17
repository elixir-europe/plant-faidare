package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * {@see http://schema.org/Dataset} as JSON-LD properties
 *
 * @author gcornut
 *
 *
 */
public interface Dataset extends JSONLD {

	/**
	 * Data catalog identifier (local unique identifier)
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("schema:identifier")
	String getIdentifier();

	/**
	 * Dataset display name
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("schema:name")
	String getName();

	/**
	 * Public website URL for this dataset
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("schema:url")
	String getUrl();

	/**
	 * Description of the dataset
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("schema:description")
	String getDescription();

	/**
	 * URI of the data catalog this dataset is part of
	 */
	@JsonView(JSONView.JSONLDView.class)
	@JsonProperty("schema:includedInDataCatalog")
	String getSourceUri();

}
