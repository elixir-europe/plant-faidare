package fr.inra.urgi.faidare.domain.data.germplasm;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.io.Serializable;


public interface DistributorInfo extends Serializable {

    @JsonView(JSONView.GnpISFields.class)
    String getAccessionNumber();

    @JsonView(JSONView.GnpISFields.class)
    ExtendedInstitute getInstitute();

    @JsonView(JSONView.GnpISFields.class)
    String getDistributionStatus();

}

