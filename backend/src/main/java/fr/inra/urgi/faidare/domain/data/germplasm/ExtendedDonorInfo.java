package fr.inra.urgi.faidare.domain.data.germplasm;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmDonorInfo;

import java.io.Serializable;


public interface ExtendedDonorInfo extends Serializable, BrapiGermplasmDonorInfo {

    @JsonView(JSONView.GnpISFields.class)
    String getDonationDate();

}
