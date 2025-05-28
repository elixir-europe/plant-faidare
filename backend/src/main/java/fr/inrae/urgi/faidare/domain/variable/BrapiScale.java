package fr.inrae.urgi.faidare.domain.variable;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
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
