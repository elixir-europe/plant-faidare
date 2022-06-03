package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.io.Serializable;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Trials/GetTrialById.md
 */
public interface BrapiContact extends Serializable {

    @JsonView(JSONView.BrapiFields.class)
    String getContactDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    String getEmail();

    @JsonView(JSONView.BrapiFields.class)
    String getType();

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteName();

    @JsonView(JSONView.BrapiFields.class)
    String getOrcid();

}
