package fr.inrae.urgi.faidare.domain.variable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Traits/ListAllTraits.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Traits/TraitDetails.md
 */
public interface BrapiTrait {
    @JsonView(JSONView.BrapiFields.class)
    String getTraitDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    @JsonProperty("class")
    String getTraitClass();

    @JsonView(JSONView.BrapiFields.class)
    String getDescription();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getSynonyms();

    @JsonView(JSONView.BrapiFields.class)
    String getMainAbbreviation();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getAlternativeAbbreviations();

    @JsonView(JSONView.BrapiFields.class)
    String getEntity();

    @JsonView(JSONView.BrapiFields.class)
    String getAttribute();

    @JsonView(JSONView.BrapiFields.class)
    String getStatus();

    @JsonView(JSONView.BrapiFields.class)
    String getXref();
}
