package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiGermplasmInstitute {
    @JsonView(JSONView.BrapiFields.class)
    String getInstituteName();

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteCode();
}
