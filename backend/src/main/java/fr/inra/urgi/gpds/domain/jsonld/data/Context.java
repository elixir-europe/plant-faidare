package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gcornut
 *
 *
 */
public class Context implements Serializable {

	private Map<String, Object> properties = new HashMap<>();

	@JsonView(JSONView.JSONLDView.class)
	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return properties;
	}

	@JsonAnySetter
	public void addProperty(String key, Object value) {
		this.properties.put(key, value);
	}

}
