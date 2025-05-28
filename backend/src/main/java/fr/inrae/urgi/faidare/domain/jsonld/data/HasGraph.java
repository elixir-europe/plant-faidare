package fr.inrae.urgi.faidare.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface HasGraph {

    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("@graph")
    List<? extends JSONLD> getGraph();

}
