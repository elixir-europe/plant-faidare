package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

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
