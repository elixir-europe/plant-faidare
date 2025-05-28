package fr.inrae.urgi.faidare.domain.variable;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
 */
public interface BrapiScaleValidValue {
    @JsonView(JSONView.BrapiFields.class)
    Double getMin();

    @JsonView(JSONView.BrapiFields.class)
    Double getMax();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getCategories();
}
