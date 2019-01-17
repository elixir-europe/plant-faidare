package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public interface BrapiMethod {
	@JsonView(JSONView.BrapiFields.class)
	String getName();

	@JsonView(JSONView.BrapiFields.class)
	@JsonProperty("class")
	String getMethodClass();

	@JsonView(JSONView.BrapiFields.class)
	String getDescription();

	@JsonView(JSONView.BrapiFields.class)
	String getFormula();

	@JsonView(JSONView.BrapiFields.class)
	String getReference();

	@JsonView(JSONView.BrapiFields.class)
	String getMethodDbId();
}
