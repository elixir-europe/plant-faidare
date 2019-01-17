package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiScaleValidValue {
	@JsonView(JSONView.BrapiFields.class)
	Double getMin();

	@JsonView(JSONView.BrapiFields.class)
	Double getMax();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getCategories();
}
