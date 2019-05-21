package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface Site {
    @JsonView(JSONView.GnpISFields.class)
    Long getSiteId();

    @JsonView(JSONView.GnpISFields.class)
    String getSiteName();

    @JsonView(JSONView.GnpISFields.class)
    Float getLatitude();

    @JsonView(JSONView.GnpISFields.class)
    Float getLongitude();

    @JsonView(JSONView.GnpISFields.class)
    String getSiteType();
}
