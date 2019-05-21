package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableOntologyList.md
 */
public interface BrapiOntology {
    @JsonView(JSONView.BrapiFields.class)
    String getOntologyName();

    @JsonView(JSONView.BrapiFields.class)
    String getOntologyDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getAuthors();

    @JsonView(JSONView.BrapiFields.class)
    String getVersion();

    @JsonView(JSONView.BrapiFields.class)
    String getCopyright();

    @JsonView(JSONView.BrapiFields.class)
    String getLicence();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiOntologyLink> getLinks();
}
