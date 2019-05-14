package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiObservationUnitTreatment {
    @JsonView(JSONView.BrapiFields.class)
    String getFactor();

    @JsonView(JSONView.BrapiFields.class)
    String getModality();
}
