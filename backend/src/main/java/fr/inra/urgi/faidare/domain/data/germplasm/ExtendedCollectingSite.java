package fr.inra.urgi.faidare.domain.data.germplasm;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmCollectingSite;

import java.io.Serializable;

public interface ExtendedCollectingSite extends Serializable, BrapiGermplasmCollectingSite {

    @JsonView(JSONView.GnpISFields.class)
    String getLocationDbId();

    @JsonView(JSONView.GnpISFields.class)
    String getSiteName();

    @JsonView(JSONView.GnpISFields.class)
    String getSiteType() ;

}
