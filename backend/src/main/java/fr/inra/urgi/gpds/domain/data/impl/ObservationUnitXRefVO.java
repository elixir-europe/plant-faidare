package fr.inra.urgi.gpds.domain.data.impl;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationUnitXRef;

/**
 * @author gcornut
 *
 *
 */
public class ObservationUnitXRefVO implements BrapiObservationUnitXRef {

	private String source;
	// Transiet solution, it would be better to apply this configuraiton to all
		// string fields globaly rather than doing it field by field.
		@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String id;

	@Override
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
