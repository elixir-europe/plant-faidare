package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiObservationUnitXRef {
    @JsonView(JSONView.BrapiFields.class)
    String getSource();

    @JsonView(JSONView.BrapiFields.class)
    String getId();
}
