package fr.inrae.urgi.faidare.domain.variable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
 */
public interface BrapiObservationVariable extends HasBrapiDocumentationURL {
    @JsonView(JSONView.BrapiFields.class)
    String getObservationVariableDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    String getOntologyDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getOntologyName();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getSynonyms();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getContextOfUse();

    @JsonView(JSONView.BrapiFields.class)
    String getGrowthStage();

    @JsonView(JSONView.BrapiFields.class)
    String getStatus();

    @JsonView(JSONView.BrapiFields.class)
    String getXref();

    @JsonView(JSONView.BrapiFields.class)
    String getInstitution();

    @JsonView(JSONView.BrapiFields.class)
    String getScientist();

    @JsonView(JSONView.BrapiFields.class)
    String getDate();

    @JsonView(JSONView.BrapiFields.class)
    String getLanguage();

    @JsonView(JSONView.BrapiFields.class)
    String getCrop();

    @JsonView(JSONView.BrapiFields.class)
    BrapiTrait getTrait();

    @JsonView(JSONView.BrapiFields.class)
    BrapiMethod getMethod();

    @JsonView(JSONView.BrapiFields.class)
    BrapiScale getScale();

    @JsonView(JSONView.BrapiFields.class)
    String getDefaultValue();

    @Override
    String getDocumentationURL();
}
