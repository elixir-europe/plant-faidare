package fr.inra.urgi.faidare.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * {@see http://schema.org/Dataset} as JSON-LD properties
 *
 * @author gcornut
 */
public interface Dataset extends JSONLD, IncludedInDataCatalog, HasURL {

    /**
     * Data catalog identifier (local unique identifier)
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:identifier")
    String getIdentifier();

    /**
     * Dataset display name
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:name")
    String getName();

    /**
     * Public website URL for this dataset
     */
    @Override
    String getUrl();

    /**
     * Description of the dataset
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:description")
    String getDescription();

    @Override
    String getSourceUri();
}
