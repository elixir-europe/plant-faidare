package fr.inra.urgi.faidare.domain.data.germplasm;
import fr.inra.urgi.faidare.domain.JSONView;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmCollectingInfo;

import java.io.Serializable;

public interface ExtendedCollectingInfo extends Serializable, BrapiGermplasmCollectingInfo {

    @JsonView(JSONView.GnpISFields.class)
    String getMaterialType();

    @JsonView(JSONView.GnpISFields.class)
    String getCollectors();

}
