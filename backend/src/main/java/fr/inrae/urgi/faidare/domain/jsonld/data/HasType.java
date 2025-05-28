package fr.inrae.urgi.faidare.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface HasType {
    /**
     * rdf:type
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("@type")
    List<String> getType();
}
