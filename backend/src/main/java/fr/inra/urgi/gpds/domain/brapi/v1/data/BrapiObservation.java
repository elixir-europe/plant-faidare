package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.Date;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiObservation {
	@JsonView(JSONView.BrapiFields.class)
	String getObservationDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getObservationVariableDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getObservationVariableName();

	@JsonView(JSONView.BrapiFields.class)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	Date getObservationTimeStamp();

	@JsonView(JSONView.BrapiFields.class)
	String getSeason();

	@JsonView(JSONView.BrapiFields.class)
	String getCollector();

	@JsonView(JSONView.BrapiFields.class)
	String getValue();
}
