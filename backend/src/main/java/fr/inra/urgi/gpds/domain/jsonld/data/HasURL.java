package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 */
public interface HasURL {
    /**
     * URL
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:url")
    String getUrl();
}
