package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Trials/GetTrialById.md
 */
public interface BrapiTrialDatasetAuthorship {

    @JsonView(JSONView.BrapiFields.class)
    String getLicense();

    @JsonView(JSONView.BrapiFields.class)
    String getDatasetPUI();

}
