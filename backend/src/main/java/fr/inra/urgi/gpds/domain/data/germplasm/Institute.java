package fr.inra.urgi.gpds.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 */
public interface Institute {
    @JsonView(JSONView.GnpISFields.class)
    String getInstituteName();

    @JsonView(JSONView.GnpISFields.class)
    String getInstituteCode();

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
