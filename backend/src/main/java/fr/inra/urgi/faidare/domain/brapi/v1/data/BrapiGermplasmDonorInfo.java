package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.data.germplasm.ExtendedInstitute;

import java.io.Serializable;

public interface BrapiGermplasmDonorInfo extends Serializable {

    @JsonView(JSONView.BrapiFields.class)
    String getDonorAccessionNumber();

    @JsonView(JSONView.BrapiFields.class)
    BrapiGermplasmInstitute getDonorInstitute();

}
