package fr.inrae.urgi.faidare.domain.variable;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableOntologyList.md
 */
public interface BrapiOntologyLink {
    @JsonView(JSONView.BrapiFields.class)
    String getRel();

    @JsonView(JSONView.BrapiFields.class)
    String getHref();
}
