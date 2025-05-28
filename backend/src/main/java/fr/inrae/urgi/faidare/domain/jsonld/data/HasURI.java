package fr.inrae.urgi.faidare.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface HasURI {
    /**
     * URI
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("@id")
    String getUri();
}
