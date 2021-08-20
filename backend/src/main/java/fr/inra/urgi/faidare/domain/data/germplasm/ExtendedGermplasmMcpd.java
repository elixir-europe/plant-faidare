package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmMcpd;
import fr.inra.urgi.faidare.domain.data.GnpISInternal;

import java.util.List;

public interface ExtendedGermplasmMcpd extends BrapiGermplasmMcpd, GnpISInternal {

    @JsonView(JSONView.GnpISFields.class)
    String getGeneticNature();

    @JsonView(JSONView.GnpISFields.class)
    String getPresenceStatus();

    @JsonView(JSONView.GnpISFields.class)
    String getRemarks();

    @JsonView(JSONView.GnpISFields.class)
    Site getOriginSite();

    @JsonView(JSONView.GnpISFields.class)
    ExtendedInstitute getHoldingInstitute();

    @JsonView(JSONView.GnpISFields.class)
    ExtendedInstitute getHoldingGenbank();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends DistributorInfo> getDistributorInfo();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends ExtendedInstitute> getBreedingInstitutes();

    @JsonView(JSONView.GnpISFields.class)
    String getBreederAccessionNumber();

    @JsonView(JSONView.GnpISFields.class)
    String getBreedingCreationYear();

    @JsonView(JSONView.GnpISFields.class)
    String getCatalogRegistrationYear();

    @JsonView(JSONView.GnpISFields.class)
    String getCatalogDeregistrationYear();
}
