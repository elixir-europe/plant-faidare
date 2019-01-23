package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface HasGraph {

    @JsonView(JSONView.JSONLDView.class)
    @JsonProperty("@graph")
    List<? extends JSONLD> getGraph();

}
