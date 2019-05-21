package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

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
