package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiGermplasmDonor {
    @JsonView(JSONView.BrapiFields.class)
    String getDonorGermplasmPUI();

    @JsonView(JSONView.BrapiFields.class)
    String getDonorAccessionNumber();

    @JsonView(JSONView.BrapiFields.class)
    String getDonorInstituteCode();
}
