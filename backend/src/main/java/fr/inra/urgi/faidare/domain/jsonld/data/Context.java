package fr.inra.urgi.faidare.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gcornut
 */
public class Context implements Serializable {

    private Map<String, Object> properties;

    public Context(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Context() {
        this.properties = new HashMap<>();
    }

    @JsonView(JSONView.JSONLDFields.class)
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

}
