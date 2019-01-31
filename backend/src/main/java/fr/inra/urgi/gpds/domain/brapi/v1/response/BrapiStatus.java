package fr.inra.urgi.gpds.domain.brapi.v1.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiStatus {
    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    String getCode();
}
