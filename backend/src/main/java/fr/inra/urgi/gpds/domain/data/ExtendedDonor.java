package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasmDonor;

/**
 * Extends the BrAPI germplasm with GnpIS fields
 *
 * @author gcornut
 */
public interface ExtendedDonor extends BrapiGermplasmDonor {

    @JsonView(JSONView.GnpISFields.class)
    Institute getDonorInstitute();

    @JsonView(JSONView.GnpISFields.class)
    Integer getDonationDate();
}
