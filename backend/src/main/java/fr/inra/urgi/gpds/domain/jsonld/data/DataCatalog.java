package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * {@see http://schema.org/DataCatalog} as JSON-LD properties
 *
 * @author gcornut
 */
public interface DataCatalog extends JSONLD, HasURL {

    /**
     * Data catalog identifier (local unique identifier)
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:identifier")
    String getIdentifier();

    /**
     * Data catalog display name
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:name")
    String getName();

    /**
     * Data catalog web site URL
     */
    @Override
    String getUrl();

    /**
     * Data catalog logo image url
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:image")
    String getImageUrl();

}
