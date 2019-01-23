package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

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
