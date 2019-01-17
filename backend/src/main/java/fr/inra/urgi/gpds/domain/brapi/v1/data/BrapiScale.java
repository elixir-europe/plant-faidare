package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiScale {
	@JsonView(JSONView.BrapiFields.class)
	String getScaleDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getName();

	@JsonView(JSONView.BrapiFields.class)
	String getDataType();

	@JsonView(JSONView.BrapiFields.class)
	String getDecimalPlaces();

	@JsonView(JSONView.BrapiFields.class)
	String getXref();

	@JsonView(JSONView.BrapiFields.class)
	BrapiScaleValidValue getValidValues();
}
