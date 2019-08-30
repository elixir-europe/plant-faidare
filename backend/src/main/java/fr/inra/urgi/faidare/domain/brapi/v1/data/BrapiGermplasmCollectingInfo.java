package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.data.germplasm.ExtendedInstitute;
import fr.inra.urgi.faidare.domain.data.germplasm.InstituteVO;

import java.io.Serializable;
import java.util.List;

public interface BrapiGermplasmCollectingInfo extends Serializable {

    @JsonView(JSONView.BrapiFields.class)
    List<? extends BrapiGermplasmInstitute> getCollectingInstitutes();

    @JsonView(JSONView.BrapiFields.class)
    String getCollectingMissionIdentifier();

    @JsonView(JSONView.BrapiFields.class)
    String getCollectingNumber();

    @JsonView(JSONView.BrapiFields.class)
    BrapiGermplasmCollectingSite getCollectingSite();

}
