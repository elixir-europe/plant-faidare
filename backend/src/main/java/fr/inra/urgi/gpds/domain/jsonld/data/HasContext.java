package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 */
public interface HasContext {

    @JsonView(JSONView.JSONLDView.class)
    @JsonProperty("@context")
    Context getContext();

}
