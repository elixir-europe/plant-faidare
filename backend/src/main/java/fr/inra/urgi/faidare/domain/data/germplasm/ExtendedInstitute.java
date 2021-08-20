package fr.inra.urgi.faidare.domain.data.germplasm;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmInstitute;

public interface ExtendedInstitute extends BrapiGermplasmInstitute {

    @JsonView(JSONView.GnpISFields.class)
    String getAcronym();

    @JsonView(JSONView.GnpISFields.class)
    String getOrganisation();

    @JsonView(JSONView.GnpISFields.class)
    String getInstituteType();

    @JsonView(JSONView.GnpISFields.class)
    String getWebSite();

    @JsonView(JSONView.GnpISFields.class)
    String getAddress();

    @JsonView(JSONView.GnpISFields.class)
    String getLogo();

}
