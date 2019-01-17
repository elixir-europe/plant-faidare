package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Additional info with a dynamic JSON mapping to a Java <code>Map<String,Object></code>
 *
 * How the dynamic mapping is configured:
 * @link http://www.cowtowncoder.com/blog/archives/2011/07/entry_458.html
 *
 * Used in BrapiLocation, BrapiStudy and BrapiTrial:
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/ListLocations.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/LocationDetails.md
 *
 * @author gcornut
 *
 *
 */
public class BrapiAdditionalInfo implements Serializable {

	private Map<String, Object> properties = new HashMap<>();

	@JsonView(JSONView.BrapiFields.class)
	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return properties;
	}

	@JsonAnySetter
	public void addProperty(String key, Object value) {
		this.properties.put(key, value);
	}

}
